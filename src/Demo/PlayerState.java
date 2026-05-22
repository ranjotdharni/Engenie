package Demo;

import Animate.Animation;
import States.ActiveState;
import States.State;
import States.StateManager;

public class PlayerState extends StateManager<Player> {
    public static final float _ANIM_FPS = 20f;

    public static final String _STATE_ALIAS_FACING_RIGHT = "_STATE_FACING_RIGHT";
    public static final String _STATE_ALIAS_FACING_LEFT = "_STATE_FACING_LEFT";
    public static final String _STATE_ALIAS_RUNNING_RIGHT = "_STATE_RUNNING_RIGHT";
    public static final String _STATE_ALIAS_RUNNING_LEFT = "_STATE_RUNNING_LEFT";

    private Animation _ANIM_IDLE = new Animation(this.entity.getPosition(), 1f, this.entity.getSpritesheet(), 1, 6, _ANIM_FPS, true, true);
    private Animation _ANIM_RUN = new Animation(this.entity.getPosition(), 1f, this.entity.getSpritesheet(), 2, 8, _ANIM_FPS, true, true);

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
        entity.setAnimation(this._ANIM_IDLE);

        if (this._ANIM_IDLE.getMirrorX() < 0f)
            this._ANIM_IDLE.horizontalFlip();
    }

    private void _APPLY_FACING_LEFT(Player entity) {
        entity.setAnimation(this._ANIM_IDLE);

        if (this._ANIM_IDLE.getMirrorX() > 0f)
            this._ANIM_IDLE.horizontalFlip();
    }

    private void _APPLY_RUNNING_RIGHT(Player entity) {
        entity.setAnimation(this._ANIM_RUN);

        if (this._ANIM_RUN.getMirrorX() < 0f)
            this._ANIM_RUN.horizontalFlip();
    }

    private void _ACTIVE_RUNNING_RIGHT(Player entity) {
        entity.movePositionX(1f);
    }

    private void _APPLY_RUNNING_LEFT(Player entity) {
        entity.setAnimation(this._ANIM_RUN);

        if (this._ANIM_RUN.getMirrorX() > 0f)
            this._ANIM_RUN.horizontalFlip();
    }

    private void _ACTIVE_RUNNING_LEFT(Player entity) {
        entity.movePositionX(-1f);
    }
}
