package Demo;

import Animate.Animation;
import States.ActiveState;
import States.State;
import States.StateManager;

public class PlayerState extends StateManager<Player> {
    public static final String _STATE_ALIAS_FACING_RIGHT = "_STATE_FACING_RIGHT";
    public static final String _STATE_ALIAS_FACING_LEFT = "_STATE_FACING_LEFT";
    public static final String _STATE_ALIAS_RUNNING_RIGHT = "_STATE_RUNNING_RIGHT";
    public static final String _STATE_ALIAS_RUNNING_LEFT = "_STATE_RUNNING_LEFT";

    private State<Player>[] states = new State[] {
            new State<Player>(_STATE_ALIAS_FACING_RIGHT, this::_APPLY_FACING_RIGHT),
            new ActiveState<Player>(_STATE_ALIAS_RUNNING_RIGHT, this::_APPLY_RUNNING_RIGHT, this::_ACTIVE_RUNNING_RIGHT),
            new State<Player>(_STATE_ALIAS_FACING_LEFT, this::_APPLY_FACING_LEFT),
            new ActiveState<Player>(_STATE_ALIAS_RUNNING_LEFT, this::_APPLY_RUNNING_LEFT, this::_ACTIVE_RUNNING_LEFT),
    };

    public PlayerState(Player entity) {
        super(entity);

        for (int i = 0; i < this.states.length; i++) {
            State<Player> state = this.states[i];
            super.addState(state);
        }
    }

    private void _APPLY_FACING_RIGHT(Player entity) {
        Animation animation = entity.getAnimation();
        animation.setAnimationIndex(1);
        animation.setFrameCount(6);
        animation.setFrameIndex(0);

        if (animation.getMirrorX() < 0f)
            animation.horizontalFlip();
    }

    private void _APPLY_FACING_LEFT(Player entity) {
        Animation animation = entity.getAnimation();
        animation.setAnimationIndex(1);
        animation.setFrameCount(6);
        animation.setFrameIndex(0);

        if (animation.getMirrorX() > 0f)
            animation.horizontalFlip();
    }

    private void _APPLY_RUNNING_RIGHT(Player entity) {
        Animation animation = entity.getAnimation();
        animation.setAnimationIndex(2);
        animation.setFrameCount(8);
        animation.setFrameIndex(0);

        if (animation.getMirrorX() < 0f)
            animation.horizontalFlip();
    }

    private void _ACTIVE_RUNNING_RIGHT(Player entity) {
        entity.movePositionX(1.5f);
    }

    private void _APPLY_RUNNING_LEFT(Player entity) {
        Animation animation = entity.getAnimation();
        animation.setAnimationIndex(2);
        animation.setFrameCount(8);
        animation.setFrameIndex(0);

        if (animation.getMirrorX() > 0f)
            animation.horizontalFlip();
    }

    private void _ACTIVE_RUNNING_LEFT(Player entity) {
        entity.movePositionX(-1.5f);
    }
}
