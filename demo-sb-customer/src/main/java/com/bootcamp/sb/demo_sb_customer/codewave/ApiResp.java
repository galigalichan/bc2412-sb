package com.bootcamp.sb.demo_sb_customer.codewave;

public class ApiResp<T> {
  private String code;
  private String message;
  private T data;

  public static <T> Builder<T> builder() {
    return new Builder<>();
  }

  public ApiResp(Builder<T> builder) {
    this.code = builder.code;
    this.message = builder.message;
    this.data = builder.data;
  }

  public String getCode() {
    return this.code;
  }

  public String getMessage() {
    return this.message;
  }

  public T getData() {
    return this.data;
  }

  public static class Builder<T> {
    private String code;
    private String message;
    private T data;

    public Builder<T> syscode(SysCode sysCode) {
      this.code = sysCode.getCode();
      this.message = sysCode.getMessage();
      return this;
    }

    // public Builder<T> code(String code) {
    //   this.code = code;
    //   return this;
    // }

    // public Builder<T> message(String message) {
    //   this.message = message;
    //   return this;
    // }

    public Builder<T> data(T data) {
      this.data = data;
      return this;
    }

    public ApiResp<T> build() {
      return new ApiResp<>(this);
    }
  }
}