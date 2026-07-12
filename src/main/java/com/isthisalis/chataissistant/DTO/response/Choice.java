package com.isthisalis.chataissistant.DTO.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Choice
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public record Choice(Message message, Integer index) {}
