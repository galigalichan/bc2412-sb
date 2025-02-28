package com.bootcamp.sb.demo_sb_bc_forum.lib;

import org.springframework.web.util.UriComponentsBuilder;

public class XUriBuilder {
    private UriComponentsBuilder uriComponentsBuilder;
    private String scheme = "https";
    private String host;
    private String path;

    public static XUriBuilder create() {
        return new XUriBuilder();
    }

    private XUriBuilder() {
        this.uriComponentsBuilder = UriComponentsBuilder.newInstance();
        this.scheme = "https";
    }

    public XUriBuilder https() {
        this.scheme = "https";
        return this;
    }

    public XUriBuilder http() { // in case of missing the "s" when typing
        this.scheme = "http";
        return this;
    }

    public XUriBuilder host(String host) {
        this.host = host;
        return this;
    }

    public XUriBuilder path(String path) {
        this.path = path;
        return this;
    }

    public String build() {
        return this.uriComponentsBuilder.scheme(this.scheme).host(this.host).path(this.path).toUriString();
    }
}
