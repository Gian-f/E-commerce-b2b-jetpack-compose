package com.br.b2b.domain.model

data class VoiceToTextParserState(
    var spokenText: String = "",
    val isSpeaking: Boolean = false,
    val error: String? = null,
)