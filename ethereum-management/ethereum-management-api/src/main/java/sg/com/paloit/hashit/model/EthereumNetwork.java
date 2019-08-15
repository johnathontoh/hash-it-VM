package sg.com.paloit.hashit.model;

import java.io.Serializable;
import java.util.UUID;

import static java.util.Optional.ofNullable;

public class EthereumNetwork implements Serializable {
    private static final long serialVersionUID = 1L;
    private UUID id;
    private String name;
    private String rpcEndpoint;
    private NetworkStatus status;
    private Integer nodes;

    public EthereumNetwork() {
    }

    public EthereumNetwork(UUID id, String name, String rpcEndpoint, NetworkStatus status, Integer nodes) {
        this.id = id;
        this.name = name;
        this.rpcEndpoint = rpcEndpoint;
        this.status = status;
        this.nodes = nodes;
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

    public NetworkStatus getStatus() {
        return status;
    }

    public Integer getNodes() {
        return nodes;
    }

    public static class EthereumNetworkBuilder {
        private UUID id;
        private String name;
        private String rpcEndpoint;
        private NetworkStatus status;
        private Integer nodes;

        public EthereumNetworkBuilder() {
        }

        public EthereumNetworkBuilder(EthereumNetwork ethereumNetwork) {
            ofNullable(ethereumNetwork.getId()).ifPresent(this::setId);
            ofNullable(ethereumNetwork.getName()).ifPresent(this::setName);
            ofNullable(ethereumNetwork.getNodes()).ifPresent(this::setNodes);
            ofNullable(ethereumNetwork.getRpcEndpoint()).ifPresent(this::setRpcEndpoint);
            ofNullable(ethereumNetwork.getStatus()).ifPresent(this::setStatus);
        }

        public EthereumNetworkBuilder setId(UUID id) {
            this.id = id;
            return this;
        }

        public EthereumNetworkBuilder setName(String name) {
            this.name = name;
            return this;
        }

        public EthereumNetworkBuilder setRpcEndpoint(String rpcEndpoint) {
            this.rpcEndpoint = rpcEndpoint;
            return this;
        }

        public EthereumNetworkBuilder setStatus(NetworkStatus status) {
            this.status = status;
            return this;
        }

        public EthereumNetworkBuilder setNodes(Integer nodes) {
            this.nodes = nodes;
            return this;
        }

        public EthereumNetwork build() {
            return new EthereumNetwork(id, name, rpcEndpoint, status, nodes);
        }
    }

    public static EthereumNetworkBuilder newBuilder() {
        return new EthereumNetworkBuilder();
    }

    public static EthereumNetworkBuilder newBuilder(EthereumNetwork ethereumNetwork) {
        return new EthereumNetworkBuilder(ethereumNetwork);
    }
}
