package com.aimlab.service.game;

import com.aimlab.domain.game.GameType;
import org.springframework.stereotype.Service;

@Service
public class PracticeGameService implements GameService{
    @Override
    public void createGame() {
    }

    @Override
    public void saveGameResult(Long gameId) {

    }

    @Override
    public GameType supportGame() {
        return GameType.PRACTICE_MODE;
    }
}
