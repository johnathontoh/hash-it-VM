pragma solidity ^0.4.25;

contract MedicalHistory {
    // This is the section where we define the configurable properties of the contract
    bool config_template_allow_basic_access_to_all_med_providers = <%=config_template_allow_basic_access_to_all_med_providers%>;
    bool config_template_allow_medical_history_read_access_to_all_med_providers = <%=config_template_allow_medical_history_read_access_to_all_med_providers%>;
    bool config_template_allow_medical_history_write_access_to_all_med_providers = <%=config_template_allow_medical_history_write_access_to_all_med_providers%>;
    bool config_template_allow_anyone_to_add_medical_provider = <%=config_template_allow_anyone_to_add_medical_provider%>;
    bool config_template_allow_anyone_to_add_users = <%=config_template_allow_anyone_to_add_users%>;

    // This contains the list of medical providers mapped to their ethereum addresses
    mapping(address => string) medicalProviders;

    // This contains the list of Users (with their government issues IDs) mapped to their ethereum addresses
    mapping(string => address) userIDs;

    struct BasicMedicalInformation {
        <% for(let i = 0; i < basicMedicalInformation.length; ++i) { %>
        <%=basicMedicalInformation[i].type%> <%=basicMedicalInformation[i].name%>;
        <% } %>
    }

    // This contains the basic medical info for every user by address
    mapping(address => BasicMedicalInformation) basicMedicalInfos;

    // Requests pending approval from medical provider to see a user's records
    mapping(address => address[]) pendingBasicInfoRequests;

    // Requests approved for a medical provider to see a user's records
    mapping(address => address[]) approvedBasicInfoRequests;

    struct MedicalRecord {
        address medicalProvider; // Address of provider who did this
        <% for(let i = 0; i < medicalRecord.length; ++i) { %>
        <%=medicalRecord[i].type%> <%=medicalRecord[i].name%>;
        <% } %>
    }

    // This contains the medical records for every user by address
    mapping(address => MedicalRecord[]) medicalRecords;

    // You can use this modifier to restrict access to certain functions to medical providers only
    modifier onlyAuthorities {
        require (!isEmpty(medicalProviders[msg.sender]), "Only an existing Medical Provider can perform this action");
        _;
    }

    // You can use this modifier to restrict access for medical provider addition
    modifier addAuthorities {
        require (!isEmpty(medicalProviders[msg.sender]) || config_template_allow_anyone_to_add_medical_provider, "Only an existing Medical Provider can perform this action");
        _;
    }

    // You can use this modifier to restrict access for adding new users (patients) to the smart contract
    modifier addUsers {
        require (!isEmpty(medicalProviders[msg.sender]) || config_template_allow_anyone_to_add_users, "Only an existing Medical Provider can perform this action");
        _;
    }

    // You can use this modifier to restrict access to who has access to the user's basic info and medical history
    modifier authorizedMedicalInfoReaders(address userAdd) {
        require(existsInArray(approvedBasicInfoRequests[userAdd], msg.sender) || (config_template_allow_medical_history_read_access_to_all_med_providers &&  !isEmpty(medicalProviders[msg.sender])));
        _;
    }

    // You can use this modifier to restrict access to who has access to add to the user's medical history
    modifier authorizedMedicalInfoWriters(address userAdd) {
        require(existsInArray(approvedBasicInfoRequests[userAdd], msg.sender) || (config_template_allow_medical_history_write_access_to_all_med_providers &&  !isEmpty(medicalProviders[msg.sender])));
        _;
    }

    // Constructor for the smart contract. Initializes the creator as the first authority
    constructor(string nameOfProvider) public {
        require (!isEmpty(nameOfProvider), "You need to provide a name for the contract creator / provider");
        medicalProviders[msg.sender] = nameOfProvider;
    }

    // Allow addition of authority
    function addAuthority(address add, string name) public addAuthorities {
        require (!isEmpty(name), "You need to provide a name for the authority you are adding");
        medicalProviders[add] = name;
    }

    // Add new user. Only authorities should be allowed to add
    function addOrUpdateUser(string ID, address add) public addUsers {
        userIDs[ID] = add;
    }

    // Add Basic medical info about yourself. A user can only modify their own
    function addBasicInfo(<%=basicMedicalInformation.map(v => v.type + " " + v.name).join(', ')%>) public {
        // Ensure sex passed in is male or female
        // require(compareStrings(sex,"M") || compareStrings(sex,"F"));
        // Ensure there are 6 characters in the dob
        // require(bytes(dob).length == 8);

        <% for(let i = 0; i < basicMedicalInformation.length; ++i) { %>
        basicMedicalInfos[msg.sender].<%=basicMedicalInformation[i].name%> = <%=basicMedicalInformation[i].name%>;
        <% } %>
    }

    // Get Basic info about a particular user
    function getBasicInfo(address userAdd) public view authorizedMedicalInfoReaders(userAdd) returns (<%=basicMedicalInformation.map(v => v.type + " " + v.name).join(', ')%>) {
        return (<%=basicMedicalInformation.map(v => "basicMedicalInfos[userAdd]."+ v.name).join(', ')%>);
    }

    // Authorities can request for access to user's basic information
    function requestBasicInfoAccess(address userAdd) public onlyAuthorities {
        require(!config_template_allow_basic_access_to_all_med_providers, "Basic access is already granted to all providers for all users");

        if(!existsInArray(pendingBasicInfoRequests[userAdd], msg.sender)) {
            // Create a request for a user to approve
            pendingBasicInfoRequests[userAdd].push(msg.sender);
        }
    }

    // To check pending requests for access to a user's basic info. Only the user himself should be able to check
    function getPendingBasicInfoRequests() public view returns (address[]) {
        return pendingBasicInfoRequests[msg.sender];
    }

    // To check pending requests for access to a user's basic info. Only the user himself should be able to check
    function getApprovedBasicInfoRequests() public view returns (address[]) {
        return approvedBasicInfoRequests[msg.sender];
    }

    // Allow users to approve basic info access request
    function approveBasicInfoAccess(address authorityAdd) public {
        // Check that there is a pending request in the first place, and there is no request which is already approved
        // Also check that the flag to allow access to all providers is not set to true
        require(!config_template_allow_basic_access_to_all_med_providers, "Basic access is already granted to all providers for all users");
        require(existsInArray(pendingBasicInfoRequests[msg.sender], authorityAdd), "Check that there is a pending access request");
        require(!existsInArray(approvedBasicInfoRequests[msg.sender], authorityAdd));

        // Move to approved providers
        approvedBasicInfoRequests[msg.sender].push(authorityAdd);

        // Delete from pending requests
        deleteFromPendingBasicInfoArray(authorityAdd);
    }

    // Allows users to revoke access to a particular provider
    function revokeBasicInfoAccess(address authorityAdd) public {
        // Check that there is an approved request in the firstPlace
        require(existsInArray(approvedBasicInfoRequests[msg.sender], authorityAdd));

        // Delete from approved requests
        deleteFromApprovedBasicInfoArray(authorityAdd);
    }

    // Add new medical record for a particular users
    function addMedicalRecord(address user, <%=medicalRecord.map(v => v.type + " " + v.name).join(', ')%>) public authorizedMedicalInfoWriters(user) {
        MedicalRecord memory record;

        <% for(let i = 0; i < medicalRecord.length; ++i) { %>
            record.<%=medicalRecord[i].name%> = <%=medicalRecord[i].name%>;
        <% } %>
        record.medicalProvider = msg.sender;
        medicalRecords[user].push(record);
    }

    // Retrieve medical records for a particular user
    function getMedicalRecords(address user, uint256 index) public view authorizedMedicalInfoReaders(user) returns (address medicalProvider, <%=medicalRecord.map(v => v.type + " " + v.name).join(', ')%>) {
        MedicalRecord memory record = medicalRecords[user][index];

        return (record.medicalProvider, <%=medicalRecord.map(v => "record." + v.name).join(', ')%>);
    }

    function getMedicalProviderName(address add) public view returns (string providerName) {
        return medicalProviders[add];
    }

    // Deletes an address from an array of addresses
    function deleteFromPendingBasicInfoArray(address addr) internal {
        uint256 index = getIndexFromArray(pendingBasicInfoRequests[msg.sender], addr);

        require (index <= pendingBasicInfoRequests[msg.sender].length);

        for (uint i = index; i<pendingBasicInfoRequests[msg.sender].length-1; i++){
            pendingBasicInfoRequests[msg.sender][i] = pendingBasicInfoRequests[msg.sender][i+1];
        }
        delete pendingBasicInfoRequests[msg.sender][pendingBasicInfoRequests[msg.sender].length-1];
        pendingBasicInfoRequests[msg.sender].length--;
    }

    // Deletes an address from an array of addresses
    function deleteFromApprovedBasicInfoArray(address addr) internal {
        uint256 index = getIndexFromArray(approvedBasicInfoRequests[msg.sender], addr);

        require (index <= approvedBasicInfoRequests[msg.sender].length);

        for (uint i = index; i<approvedBasicInfoRequests[msg.sender].length-1; i++){
            approvedBasicInfoRequests[msg.sender][i] = approvedBasicInfoRequests[msg.sender][i+1];
        }
        delete approvedBasicInfoRequests[msg.sender][approvedBasicInfoRequests[msg.sender].length-1];
        approvedBasicInfoRequests[msg.sender].length--;
    }

    // Retrieves the index of an address from the pendingBasicInfoRequests
    function getIndexFromArray(address[] array, address addr) internal pure returns(uint256) {
        for (uint256 i=0; i<array.length; i++) {
            if (array[i] == addr) {
                return i;
            }
        }

        revert();
    }

    // Checks if an address exists in an array of addresses
    function existsInArray(address[] array, address addr) internal pure returns (bool) {
        for (uint256 i=0; i<array.length; i++) {
            if (array[i] == addr) {
                return true;
            }
        }

        return false;
    }

    // Checks if a string is empty
    function isEmpty(string str) internal pure returns (bool) {
        // Convert str to bytes, and check the length of the byte array
        bytes memory byteArray = bytes(str); // Uses memory
        if (byteArray.length == 0) {
            return true;
        }

        return false;
    }

    function compareStrings(string a, string b) internal pure returns (bool) {
        if(bytes(a).length != bytes(b).length) {
            return false;
        }
        else {
            return keccak256(abi.encodePacked(a)) == keccak256(abi.encodePacked(b));
        }
    }
}