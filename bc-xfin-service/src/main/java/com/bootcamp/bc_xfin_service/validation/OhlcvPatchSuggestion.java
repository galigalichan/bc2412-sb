package com.bootcamp.bc_xfin_service.validation;

public record OhlcvPatchSuggestion(Long id, Long oldTimestamp, Long newTimestamp) {}