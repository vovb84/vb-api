/*
 * Country Code Lookup API
 * API for conversion country code to country name from remote API https://www.travel-advisory.info/api
 *
 * OpenAPI spec version: 1.0.0
 * 
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */


package org.vb.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.*;

/**
 * Country Name to Country Code JSONObject.
 */
@ApiModel(description = "Country Name to Country Code JSONObject.")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2020-10-24T21:02:39.389-07:00")
public class CountryCode   {
  @JsonProperty("countryName")
  private String countryName = null;

  @JsonProperty("countryCode")
  private String countryCode = null;

  public CountryCode countryName(String countryName) {
    this.countryName = countryName;
    return this;
  }

  /**
   * Country Name.
   * @return countryName
   **/
  @JsonProperty("countryName")
  @ApiModelProperty(required = true, value = "Country Name.")
  @NotNull
  public String getCountryName() {
    return countryName;
  }

  public void setCountryName(String countryName) {
    this.countryName = countryName;
  }

  public CountryCode countryCode(String countryCode) {
    this.countryCode = countryCode;
    return this;
  }

  /**
   * Country Code.
   * @return countryCode
   **/
  @JsonProperty("countryCode")
  @ApiModelProperty(required = true, value = "Country Code.")
  @NotNull
  public String getCountryCode() {
    return countryCode;
  }

  public void setCountryCode(String countryCode) {
    this.countryCode = countryCode;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CountryCode countryCode = (CountryCode) o;
    return Objects.equals(this.countryName, countryCode.countryName) &&
        Objects.equals(this.countryCode, countryCode.countryCode);
  }

  @Override
  public int hashCode() {
    return Objects.hash(countryName, countryCode);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CountryCode {\n");
    
    sb.append("    countryName: ").append(toIndentedString(countryName)).append("\n");
    sb.append("    countryCode: ").append(toIndentedString(countryCode)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

