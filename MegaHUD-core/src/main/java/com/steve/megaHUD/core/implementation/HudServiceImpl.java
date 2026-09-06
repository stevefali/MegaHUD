package com.steve.megaHUD.core.implementation;

import com.steve.MegaHUD.api.HudService;
import com.steve.megaHUD.core.hudstate.HudStateManager;

import java.util.UUID;

public class HudServiceImpl implements HudService {

    @Override
    public void registerPlayer(UUID playerId) {
        HudStateManager.registerPlayer(playerId);
    }

    @Override
    public void unregisterPlayer(UUID playerId) {
        HudStateManager.unregisterPlayer(playerId);
    }

    @Override
    public void clearAll() {
        HudStateManager.clear();
    }
}
