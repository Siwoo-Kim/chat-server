package com.siwoo.client;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.net.InetAddress;

@Getter @ToString @EqualsAndHashCode(of = "name")
public class Client {
    private final Long id;
    private final String name;
    private final InetAddress hostAddress;

    private Client(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.hostAddress = builder.hostAddress;
    }

    public static class Builder {
        private Long id;
        private String name;
        private InetAddress hostAddress;

        public Builder id(long id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder hostAddress(InetAddress hostAddress) {
            this.hostAddress = hostAddress;
            return this;
        }

        public Client build() {
            return new Client(this);
        }
    }

}
