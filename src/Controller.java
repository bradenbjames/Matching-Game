import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import javafx.util.Duration;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.Stack;

public class Controller implements Initializable {

    private final int boardLength = 4;
    private final int boardSize = boardLength * boardLength;
    SimpleIntegerProperty counter = new SimpleIntegerProperty(0);

    // text label to display the timeline
    @FXML
    private Label timerLabel;

    // match feedback text
    @FXML
    private Text text;

    // keeping track of button emoji
    Stack<String> stack = new Stack<String>();

    // keeping track of last button clicked
    Stack<Button> buttonStack = new Stack<Button>();

    // list of all buttons
    ArrayList<Button> buttonList = new ArrayList<>();

    Timeline timelineSeconds = new Timeline(new KeyFrame(Duration.seconds(1.0), e -> {
        this.counter.set(counter.get() + 1);
        this.timerLabel.setText("Timer (seconds): " + counter.getValue());
    }));

    Timeline timelineBlink = new Timeline(new KeyFrame(Duration.seconds(0.5), evt -> text.setVisible(false)),
            new KeyFrame(Duration.seconds(0.1), evt -> text.setVisible(true)));

    Random random = new Random();

    // boolean for checking 1st or 2nd turn
    boolean onSecondTurn = false;

    // counts to 8, indicating all matches made
    private int matchCounter = 0;

    // emoji list
    ArrayList<String> possibleButtons = new ArrayList<>(
            Arrays.asList("🙈", "🙈", "🐀", "🐀", "🦍", "🦍", "🦇", "🦇", "🐖", "🐖", "🐹", "🐹", "🐼", "🐼",
                    "🦉", "🦉"));
    // buttons
    @FXML
    private Button button0, button1, button2, button3, button4, button5, button6,
            button7, button8, button9, button10, button11, button12, button13, button14, button15;

    // list of all buttons on scene
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        buttonList.addAll(Arrays.asList(button0, button1, button2, button3, button4, button5, button6,
                button7, button8, button9, button10, button11, button12, button13, button14, button15));
    }

    // button clicked
    @FXML
    void buttonClicked(ActionEvent event) {
        String buttonID = ((Control) event.getSource()).getId();
        // do if it's 1st turn
        if (!this.onSecondTurn) {
            for (Button b : buttonList) {
                if (b.getId() == buttonID) {
                    this.buttonStack.push(b);
                    this.stack.push(b.getText());
                }
            }
            onSecondTurn = true;
        }
        // do if it's 1st turn
        else {
            for (Button a : buttonList) {
                if (a.getId() == buttonID) {
                    if (a.getText() == stack.peek()) {
                        Button secondButton = buttonStack.peek();
                        if (a.equals(secondButton)) {
                            text.setText("Cannot click same item");
                        } else {
                            matchCounter++;
                            text.setText("Match!");
                            hideButtons(a, secondButton);
                        }
                    } else {
                        this.text.setText("No Match!");
                    }
                }
            }
            onSecondTurn = false;
            if (matchCounter == 8) {
                text.setText("Match Won in " + this.counter.get() + " seconds!");
                timelineBlink.setCycleCount(Animation.INDEFINITE);
                timelineBlink.play();
                timelineSeconds.stop();
            }
        }
    }

    public void hideButtons(Button a, Button b) {
        a.setDisable(true);
        b.setDisable(true);
    }

    @FXML
    void start(ActionEvent event) {
        // this.timeline.stop();
        this.counter.set(0);
        this.timelineSeconds.setCycleCount(Timeline.INDEFINITE);
        this.timelineSeconds.play();
        Collections.shuffle(possibleButtons);
        for (int i = 0; i < this.boardSize; i++) {
            buttonList.get(i).setText(possibleButtons.get(i));
            buttonList.get(i).setDisable(false);
        }
    }
}
