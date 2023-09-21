package com.atnerix.alyx.player

import com.atnerix.alyx.api.SlashInteraction

data class PlayAction(
    val interaction: SlashInteraction,
    val trackUrl: String
)
