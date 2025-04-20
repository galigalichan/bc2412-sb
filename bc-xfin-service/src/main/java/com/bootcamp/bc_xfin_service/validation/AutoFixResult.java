package com.bootcamp.bc_xfin_service.validation;

import java.util.List;

public record AutoFixResult(List<OhlcvPatchSuggestion> applied, List<OhlcvPatchSuggestion> failed) {}
