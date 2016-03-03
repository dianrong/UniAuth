package com.dianrong.common.techops.bean;

import java.util.List;

/**
 * Created by Arc on 2/3/2016.
 */
public class Node {

    private String id;
    private String label;
    private String code;
    private String type;
    private String description;

    private List<Node> children;

    public String getId() {
        return id;
    }

    public Node setId(String id) {
        this.id = id;
        return this;
    }

    public String getLabel() {
        return label;
    }

    public Node setLabel(String label) {
        this.label = label;
        return this;
    }

    public String getType() {
        return type;
    }

    public Node setType(String type) {
        this.type = type;
        return this;
    }

    public List<Node> getChildren() {
        return children;
    }

    public Node setChildren(List<Node> children) {
        this.children = children;
        return this;
    }

    public String getCode() {
        return code;
    }

    public Node setCode(String code) {
        this.code = code;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Node setDescription(String description) {
        this.description = description;
        return this;
    }
}
