package components;

public interface KeyActivityListener {
    void onKeyActivity(int[] keyCodes);

    void onKeyTyped(int extendedKeyCode);
}
