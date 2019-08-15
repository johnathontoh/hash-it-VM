pragma solidity ^0.4.25;
contract Settlement {
    
    mapping(address => mapping(address => int256)) comptes;
    
    function AddMoneyTo (address foreign, int256 amountToAdd) public {
        /// This function adds money to the account specified.
        comptes[msg.sender][foreign] += amountToAdd;
    }

    function ShowMoneyOf(address foreign) public view returns (int256 _thebalanceofmoney) {
        /// This function returns the amount set to this account
        _thebalanceofmoney = comptes[msg.sender][foreign];
        
    }

    function MagicMoneyTo (address foreign, address origin, int256 amountToAdd) public {
        /// This function IS FOR TEST PURPOSES ONLY AND NEED TO BE REMOVED BEFORE PROD
        /// It's used to simulate sending a certain amount of money to / from a specified account.
        comptes[origin][foreign] += amountToAdd;
    }


    function PaidToTheAccount(address foreign, int256 amountPaid) public {
        comptes[msg.sender][foreign] -= amountPaid;
    }

    function NetMyAccountWithViewOnly(address foreign) public view returns (int256 _thebalanceofmoney, int256 _theotherbalanceofmoney, int256 _difference) {
        /// This function displays the values computed for the netting, without making a change
        /// Note that the value as INT is wrong most of the time, this needs to be fixed (issue is signed vs unsigned INT)
        _thebalanceofmoney = comptes[msg.sender][foreign];
        _theotherbalanceofmoney = comptes[foreign][msg.sender];
        if (_thebalanceofmoney > _theotherbalanceofmoney) {
            _thebalanceofmoney = _thebalanceofmoney - _theotherbalanceofmoney;
            _theotherbalanceofmoney = 0;
        } else {
            _theotherbalanceofmoney = _theotherbalanceofmoney - _thebalanceofmoney;
            _thebalanceofmoney = 0;
        }
        _difference = _thebalanceofmoney - _theotherbalanceofmoney;
    }
        function NetMyAccountWithUpdate(address foreign) public returns (int256 _thebalanceofmoney, int256 _theotherbalanceofmoney, int256 _difference) {
        /// This function displays AND UPDATES the values computed for the netting, by basically making SCF to the two accounts.
        /// Note that the value as INT is wrong most of the time, this needs to be fixed (issue is signed vs unsigned INT)
        /// The ACCOUNT value though is correct.
        if (comptes[msg.sender][foreign] > comptes[foreign][msg.sender]) {
            comptes[msg.sender][foreign] = comptes[msg.sender][foreign] - comptes[foreign][msg.sender];
            comptes[foreign][msg.sender] = 0;
        } else {
            comptes[foreign][msg.sender] = comptes[foreign][msg.sender] - comptes[msg.sender][foreign];
            comptes[msg.sender][foreign] = 0;
        }
        _thebalanceofmoney = comptes[msg.sender][foreign];
        _theotherbalanceofmoney = comptes[foreign][msg.sender];
        _difference = comptes[msg.sender][foreign] - comptes[foreign][msg.sender];
    }
        
}   