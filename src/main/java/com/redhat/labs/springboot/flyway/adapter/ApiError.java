package com.redhat.labs.springboot.flyway.adapter;

import java.util.ArrayList;
import java.util.List;

public class ApiError {
  private String title;
  private String detail;
  private List<InvalidParam> invalidParams;

  public ApiError(String title, String detail) {
    this(title, detail, new ArrayList<>());
  }
  public ApiError(String title, String detail, List<InvalidParam> invalidParams) {
    this.title = title;
    this.detail = detail;
    this.invalidParams = invalidParams;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getDetail() {
    return detail;
  }

  public void setDetail(String detail) {
    this.detail = detail;
  }

  public List<InvalidParam> getInvalidParams() {
    return invalidParams;
  }

  public void setInvalidParams(List<InvalidParam> invalidParams) {
    this.invalidParams = invalidParams;
  }

  public void addInvalidParam(String name, String reason) {
    this.invalidParams.add(new InvalidParam(name, reason));
  }

  public static class InvalidParam {
    private String name;
    private String reason;
    
    public InvalidParam(String name, String reason) {
      this.name = name;
      this.reason = reason;
    }
    public String getName() {
      return name;
    }
    public void setName(String name) {
      this.name = name;
    }
    public String getReason() {
      return reason;
    }
    public void setReason(String reason) {
      this.reason = reason;
    }
  }

}
