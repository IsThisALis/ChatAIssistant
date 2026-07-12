package com.isthisalis.chataissistant.DTO.request;

import java.util.List;

/**
 * AiRequest
 */
public record AiRequest(String model, List<Message> messages, List<Tool> tools) {}
