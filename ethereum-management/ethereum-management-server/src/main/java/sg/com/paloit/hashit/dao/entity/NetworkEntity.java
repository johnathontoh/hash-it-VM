package sg.com.paloit.hashit.dao.entity;

import org.hibernate.annotations.GenericGenerator;
import sg.com.paloit.hashit.model.NetworkStatus;

import javax.persistence.*;
import java.util.UUID;

import static java.util.Optional.ofNullable;

@Entity
@Table(name = "NETWORK")
public class NetworkEntity {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "RAW(16)")
    private UUID id;
    @Column(name = "NAME")
    private String name;
    @Column(name = "RPC_ENDPOINT")
    private String rpcEndpoint;
    @Column(name = "WALLET")
    private String wallet;
    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS")
    private NetworkStatus status;
    @Column(name = "NODES")
    private Integer nodes;
    @Column(name = "JOB_ID")
    private String jobId;
    @Column(name = "DELETE_JOB_ID")
    private String deleteJobId;

    public NetworkEntity() {
    }

    public NetworkEntity(UUID id, String name, String rpcEndpoint, String wallet, NetworkStatus status,
                         Integer nodes, String jobId, String deleteJobId) {
        this.id = id;
        this.name = name;
        this.rpcEndpoint = rpcEndpoint;
        this.wallet = wallet;
        this.status = status;
        this.nodes = nodes;
        this.jobId =jobId;
        this.deleteJobId = deleteJobId;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getRpcEndpoint() {
        return rpcEndpoint;
    }

    public String getWallet() {
        return wallet;
    }

    public NetworkStatus getStatus() {
        return status;
    }

    public Integer getNodes() {
        return nodes;
    }

    public String getJobId() {
        return jobId;
    }

    public String getDeleteJobId() {
        return deleteJobId;
    }

    public static class NetworkEntityBuilder {
        private UUID id;
        private String name;
        private String rpcEndpoint;
        private String wallet;
        private NetworkStatus status;
        private Integer nodes;
        private String jobId;
        private String deleteJobId;

        public NetworkEntityBuilder() {
        }

        public NetworkEntityBuilder(NetworkEntity entity) {
            ofNullable(entity.getId()).ifPresent(this::setId);
            ofNullable(entity.getJobId()).ifPresent(this::setJobId);
            ofNullable(entity.getName()).ifPresent(this::setName);
            ofNullable(entity.getNodes()).ifPresent(this::setNodes);
            ofNullable(entity.getRpcEndpoint()).ifPresent(this::setRpcEndpoint);
            ofNullable(entity.getStatus()).ifPresent(this::setStatus);
            ofNullable(entity.getWallet()).ifPresent(this::setWallet);
            ofNullable(entity.getDeleteJobId()).ifPresent(this::setDeleteJobId);
        }

        public NetworkEntityBuilder setId(UUID id) {
            this.id = id;
            return this;
        }

        public NetworkEntityBuilder setName(String name) {
            this.name = name;
            return this;
        }

        public NetworkEntityBuilder setRpcEndpoint(String rpcEndpoint) {
            this.rpcEndpoint = rpcEndpoint;
            return this;
        }

        public NetworkEntityBuilder setWallet(String wallet) {
            this.wallet = wallet;
            return this;
        }

        public NetworkEntityBuilder setStatus(NetworkStatus status) {
            this.status = status;
            return this;
        }

        public NetworkEntityBuilder setNodes(Integer nodes) {
            this.nodes = nodes;
            return this;
        }

        public NetworkEntityBuilder setJobId(String jobId) {
            this.jobId = jobId;
            return this;
        }

        public NetworkEntityBuilder setDeleteJobId(String deleteJobId) {
            this.deleteJobId = deleteJobId;
            return this;
        }

        public NetworkEntity build() {
            return new NetworkEntity(id, name, rpcEndpoint, wallet, status, nodes, jobId, deleteJobId);
        }
    }

    public static NetworkEntityBuilder newBuilder() {
        return new NetworkEntityBuilder();
    }

    public static NetworkEntityBuilder newBuilder(NetworkEntity entity) {
        return new NetworkEntityBuilder(entity);
    }
}
