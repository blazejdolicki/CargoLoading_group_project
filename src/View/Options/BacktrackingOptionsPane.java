package View.Options;

import Shapes.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import Model.ContainerModel;
import Model.PentoContainer;
import Shapes.ParcelA;
import Shapes.ParcelB;
import Shapes.ParcelC;
import Shapes.ParcelShape;
import View.ContainerPane;
import View.ContainerView;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;

/**
 * This class will create a Pane that displays Backtracking options
 * @author Basia, Jordan
 * @version 2.0
 * @date 23.01.2018
 */
public class BacktrackingOptionsPane extends VBox{
    private Stage stage;
    private ContainerPane container;
    private ArrayList<Shape> containedShapes = new ArrayList<>();
    private ContainerModel solver;
    
    /**
     * Constructor creates a pane with options
     * @param container an instance of current shown ContainerPane
     * @param options an instance of OptionsPane to control shown options
     * @param view an instance of ContainerView to control shown container
     */
    public BacktrackingOptionsPane(ContainerPane container, OptionsPane options, ContainerView view){
        this.container = container;
        
        Label title = new Label("Choose packing:");
        title.setFont(new Font("Arial", 20));
        title.setFocusTraversable(false);
        getChildren().add(title);
        
        Button packBox = new Button("Pack boxes");
        packBox.setStyle("-fx-font: 22 arial; -fx-base: #6495ED ");
        packBox.setFocusTraversable(false);
        packBox.setMinSize(225, 50);
        RadioButton infiniteAmount = new RadioButton("Infinite amount of boxes");
        RadioButton setAmount = new RadioButton("Custom amount of boxes");
        packBox.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent e){
                view.hideButtons();
                if(setAmount.isSelected()){
                    ParcelsSet set = new ParcelsSet(getButtonPane());
                    Scene scene = new Scene(set);
                    
                    stage = new Stage();
                    stage.setScene(scene);
                    stage.setWidth(450);
                    stage.setHeight(500);
                    stage.setResizable(false);
                    stage.initModality(Modality.APPLICATION_MODAL); 
                    stage.show();
                }
                else{
                    setPackingOrder();
                }
            }});
        getChildren().add(packBox);
        
        ToggleGroup packBoxGroup = new ToggleGroup();
        infiniteAmount.setFont(new Font("Arial", 15));
        infiniteAmount.setSelected(true);
        infiniteAmount.setFocusTraversable(false);
        infiniteAmount.setToggleGroup(packBoxGroup);
        getChildren().add(infiniteAmount);
        
        setAmount.setFont(new Font("Arial", 15));
        setAmount.setFocusTraversable(false);
        setAmount.setToggleGroup(packBoxGroup);
        getChildren().add(setAmount);
        
        Button packPentominoes = new Button("Pack pentominoes");
        packPentominoes.setFocusTraversable(false);
        packPentominoes.setStyle("-fx-font: 22 arial; -fx-base: #6495ED ");
        packPentominoes.setFocusTraversable(false);
        packPentominoes.setMinSize(225, 50);
        RadioButton fill = new RadioButton("Infinite amount pentominoes");
        RadioButton maximum = new RadioButton("Set amount of pentominoes");
        packPentominoes.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent e){
                view.hideButtons();
                if(maximum.isSelected()){
                    PentominoSet set = new PentominoSet(getButtonPane());
                    Scene scene = new Scene(set);
                    
                    stage = new Stage();
                    stage.setScene(scene);
                    stage.setWidth(450);
                    stage.setHeight(500);
                    stage.setResizable(false);
                    stage.initModality(Modality.APPLICATION_MODAL); 
                    stage.show();
                }
                else{ 
                        setPackingOrderPentominoes();
                }
            }});
        getChildren().add(packPentominoes);

        ToggleGroup packPentoGroup = new ToggleGroup();

        fill.setFont(new Font("Arial", 15));
        fill.setToggleGroup(packPentoGroup);
        fill.setSelected(true);
        fill.setFocusTraversable(false);
        getChildren().add(fill);
        
        maximum.setFont(new Font("Arial", 15));
        maximum.setToggleGroup(packPentoGroup);
        maximum.setFocusTraversable(false);
        getChildren().add(maximum);
        
        Button shownContainers = new Button("Generated Containers");
        shownContainers.setFocusTraversable(false);
        shownContainers.setStyle("-fx-font: 22 arial; -fx-base: #6495ED ");
        shownContainers.setFocusTraversable(false);
        shownContainers.setMinSize(225, 50);
        shownContainers.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent e){
                if(container.getSizeShownContainers() != 0){
                    view.showButtons();
                    view.setSize(container.getSizeShownContainers());
                }
            }});
        getChildren().add(shownContainers);
        
        Button back = new Button("Back");
        back.setStyle("-fx-font: 22 arial; -fx-base: #6495ED ");
        back.setMinSize(150, 50);
        back.setFocusTraversable(false);
        back.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent e){
                view.hideButtons();
                container.redrawContainer();
                options.drawAlgorithmOptions();
            }});
        getChildren().add(back);
        
        setSpacing(15);
        setAlignment(Pos.CENTER);
    }
    /**
     * This method generates a new stage that will ask the user to specify how he wants the backtracking to sort.
     * It will give options as:(by value, by ratio).
     */
    public void setPackingOrder(){
        VBox pane = new VBox();
        Label title = new Label("Choose packing order:");
        title.setFont(new Font("Arial", 19));
        pane.getChildren().add(title);
        
        ToggleGroup packingOrderGroup = new ToggleGroup();
        
        RadioButton value = new RadioButton("By value");
        value.setFocusTraversable(false);
        value.setFont(new Font("Arial", 15));
        value.setToggleGroup(packingOrderGroup);
        value.setSelected(true);
        pane.getChildren().add(value);
        
        RadioButton ratio  = new RadioButton("By value/volume ratio");
        ratio.setFocusTraversable(false);
        ratio.setFont(new Font("Arial", 15));
        ratio.setToggleGroup(packingOrderGroup);
        pane.getChildren().add(ratio);
        
        Label timer = new Label("Set timer for backtracking:");
        timer.setFont(new Font("Arial", 19));
        pane.getChildren().add(timer);
        
        TextField Timer = new TextField();
        Timer.setMaxWidth(175);
        pane.getChildren().add(Timer);
        
        try {
            FileInputStream input = new FileInputStream("background.jpeg");
            pane.setBackground(new Background(new BackgroundImage(new Image(input,1000,1000,false,true),
            BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
            BackgroundSize.DEFAULT)));
        } catch (FileNotFoundException ex) {
            pane.setBackground(new Background(new BackgroundFill(Color.rgb(186, 216, 227), CornerRadii.EMPTY, new Insets(0, 0, 0, 0))));
        }     
        Button pack = new Button("Pack");
        pack.setStyle("-fx-font: 22 arial; -fx-base: #6495ED ");
        pack.setMinSize(150, 50);
        
        pane.getChildren().add(pack);
        pane.setSpacing(5);
        pane.setAlignment(Pos.CENTER);
        
        Scene scene = new Scene(pane);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setWidth(300);
        stage.setHeight(300);
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL); 
        stage.show();
        pack.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent e){
                stage.close();
                if(ratio.isSelected()){
                    generateSolution(ORDER.RATIO, Integer.parseInt(Timer.getText()));
                }
                else if(value.isSelected()){
                    generateSolution(ORDER.VALUE, Integer.parseInt(Timer.getText()));
                }
        }});
    }
    
    /**
     * This method generates a new stage that will ask the user to specify how he wants the backtracking to sort.
     * It will give options as:(by value, by ratio).
     */
     public void setPackingOrderPentominoes(){
        VBox pane = new VBox();
        Label title = new Label("Choose packing order:");
        title.setFont(new Font("Arial", 19));
        pane.getChildren().add(title);
        
        ToggleGroup packingOrderGroup = new ToggleGroup();
        
        RadioButton value = new RadioButton("By value");
        value.setFocusTraversable(false);
        value.setFont(new Font("Arial", 15));
        value.setToggleGroup(packingOrderGroup);
        value.setSelected(true);
        pane.getChildren().add(value);
        
        RadioButton ratio  = new RadioButton("By value/volume ratio");
        ratio.setFocusTraversable(false);
        ratio.setFont(new Font("Arial", 15));
        ratio.setToggleGroup(packingOrderGroup);
        pane.getChildren().add(ratio);
        
        Label timer = new Label("Set timer for backtracking:");
        timer.setFont(new Font("Arial", 19));
        pane.getChildren().add(timer);
        
        TextField Timer = new TextField();
        Timer.setMaxWidth(175);
        pane.getChildren().add(Timer);
        
        try {
            FileInputStream input = new FileInputStream("background.jpeg");
            pane.setBackground(new Background(new BackgroundImage(new Image(input,1000,1000,false,true),
            BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
            BackgroundSize.DEFAULT)));
        } catch (FileNotFoundException ex) {
            pane.setBackground(new Background(new BackgroundFill(Color.rgb(186, 216, 227), CornerRadii.EMPTY, new Insets(0, 0, 0, 0))));
        }     
        Button pack = new Button("Pack");
        pack.setStyle("-fx-font: 22 arial; -fx-base: #6495ED ");
        pack.setMinSize(150, 50);
        
        pane.getChildren().add(pack);
        pane.setSpacing(5);
        pane.setAlignment(Pos.CENTER);
        
        Scene scene = new Scene(pane);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setWidth(300);
        stage.setHeight(300);
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL); 
        stage.show();
        pack.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent e){
                stage.close();
                PentoContainer testContainer = new PentoContainer();
                ArrayList<Shape> givenParcels = new ArrayList<>();
                givenParcels.add(new PentominoP());
                givenParcels.add(new PentominoT());
                givenParcels.add(new PentominoL());
                if(value.isSelected()){
                    givenParcels = testContainer.orderParcelListByValue(givenParcels);
                }
                else if(ratio.isSelected()){
                    givenParcels = testContainer.orderParcelListByRatio(givenParcels);
                }
                testContainer.setParcelList(givenParcels);
                testContainer.setDelay((Integer.parseInt(Timer.getText())*1000));
                PentoContainer maxValueContainer = new PentoContainer();
                maxValueContainer.setParcelList(givenParcels);
                testContainer.loadContainer(maxValueContainer,true,true);
                container.drawPentominoes(testContainer.getContainedParcels(), testContainer.computeTotalValue(), "BacktrackingPentominoes, infinite amount of pentominoes, timer: " + (Integer.parseInt(Timer.getText())) + " seconds");
        }});
    }
    /**
     * If the user chooses option set amount of boxes and submits them this method will be called.
     * It will close the other stage and generate Solutions according to the set amount of boxes,
     * give timer and Order type.
     * @param a number of boxes typeA
     * @param b number of boxes typeB
     * @param c number of boxes typeC
     * @param order Order type, used by backtracking
     * @param timer representing the timer of backtracking
     */
    public void solveSetAmountBoxes(int a, int b, int c, ORDER order, int timer){
        stage.close();
        generateSolution(a, b, c, order, timer);
    }
    
    /**
     * If the user chooses option set amount of pentominoes and submits them this method will be called.
     * It will close the other stage and generate Solutions according to the set amount of boxes,
     * give timer and Order type.
     * @param a number of boxes typeA
     * @param b number of boxes typeB
     * @param c number of boxes typeC
     * @param order Order type, used by backtracking
     * @param timer representing the timer of backtracking
     */
    public void solveSetAmountPentominoes(int a, int b, int c, ORDER order, int timer){
        stage.close();
        generateSolutionPentominoes(a, b, c, order, timer);
    }
    /**
     * This is the actual method that generates an instance of ContainerModel. Tries to solve the container and
     * at the end draws the best found result.
     * @param order order type, used by backtracking
     * @param timer representing the timer of backtracking
     */
    public void generateSolution(ORDER order, int timer){
        ArrayList<Shape> givenParcels = new ArrayList<>();
        
            givenParcels.add(new ParcelA());
            givenParcels.add(new ParcelB());
            givenParcels.add(new ParcelC());

        solver = new ContainerModel();
        solver.setDelay(timer*1000);
        ContainerModel maxValueContainer = new ContainerModel();
        switch(order){
            case VALUE :    givenParcels = solver.orderParcelListByValue(givenParcels); 
                            solver.setParcelList(givenParcels);
                            solver.solveBacktracking(maxValueContainer, true, true);
                            containedShapes = solver.getContainedParcels();
                            container.drawBoxes(containedShapes, solver.computeTotalValue(), "Backtracking, By value order, infinite amount boxes, timer: " + timer);
                            break;
            case RATIO :    givenParcels = solver.orderParcelListByRatio(givenParcels);
                            solver.setParcelList(givenParcels);
                            solver.solveBacktracking(maxValueContainer, true, true);
                            containedShapes = solver.getContainedParcels();
                            container.drawBoxes(containedShapes, solver.computeTotalValue(), "Backtracking, By ratio order, infinite amount boxes, timer: " + timer);
                            break;                              
        }
    }
    /**
     * This is the actual method that generates an instance of ContainerModel. Tries to solve the container with
     * give set of boxes and at the end draws the best found result.
     * @param a number of boxes typeA
     * @param b number of boxes typeB
     * @param c number of boxes typeC
     * @param order order type, used by backtracking
     * @param timer representing the timer of backtracking
     */
    public void generateSolution(int a, int b, int c, ORDER order, int timer){
        ArrayList<Shape> givenParcels = new ArrayList<>();
        
            givenParcels.add(new ParcelA());
            givenParcels.add(new ParcelB());
            givenParcels.add(new ParcelC());

        solver = new ContainerModel();
        solver.setDelay(timer*1000);
        ContainerModel maxValueContainer = new ContainerModel();
        switch(order){
            case VALUE :    givenParcels = solver.orderParcelListByValue(givenParcels); 
                            solver.setParcelList(givenParcels);
                            solver.setAmountOfParcels(a, b, c);
                            solver.solveBacktracking(maxValueContainer, true, true);
                            containedShapes = solver.getContainedParcels();
                            container.drawBoxes(containedShapes, solver.computeTotalValue(), "Backtracking, By value order, TypeA: "+ a + " TypeB: " + b + " TypeC: " + c + " timer: " + timer);
                            
                            break;
            case RATIO :    givenParcels = solver.orderParcelListByRatio(givenParcels);
                            solver.setParcelList(givenParcels);
                            solver.setAmountOfParcels(a, b, c);solver.solveBacktracking(maxValueContainer, true, true);
                            containedShapes = solver.getContainedParcels();
                            container.drawBoxes(containedShapes, solver.computeTotalValue(), "Backtracking, By ratio order, TypeA: "+ a + " TypeB: " + b + " TypeC: " + c + " timer: " + timer);
                            
                            break;
        }
    }
    
    /**
     * This is the actual method that generates an instance of ContainerModel. Tries to solve the container with
     * give set of pentominoes and at the end draws the best found result.
     * @param a number of pentominoes l
     * @param b number of pentominoes p
     * @param c number of pentominoes t
     * @param order order type, used by backtracking
     * @param timer representing the timer of backtracking
     */
    public void generateSolutionPentominoes(int a, int b, int c, ORDER order, int timer){
        PentoContainer testContainer = new PentoContainer();
       ArrayList<Shape> givenParcels = new ArrayList<>();
       givenParcels.add(new PentominoP());
        givenParcels.add(new PentominoT());
        givenParcels.add(new PentominoL());
        switch(order){
            case VALUE :    
                            givenParcels = testContainer.orderParcelListByValue(givenParcels);
                            break;
            case RATIO :    givenParcels = testContainer.orderParcelListByRatio(givenParcels); 
                            break;
        }
        
        testContainer.setParcelList(givenParcels);
        testContainer.setDelay(timer*1000);
        PentoContainer maxValueContainer = new PentoContainer();
        maxValueContainer.setParcelList(givenParcels);
        testContainer.setAmountOfParcels(a, b, c);
        testContainer.loadContainer(maxValueContainer,true,true);
        container.drawPentominoes(testContainer.getContainedParcels(), testContainer.computeTotalValue(), "BacktrackingPentominoes, L: " + a + " P: " + b + " T: " + c + " timer: " + timer + " seconds");
       
    }
    /**
     * A method that returns an instance of this class
     * @return instance of this class
     */
    public BacktrackingOptionsPane getButtonPane(){
        return this;
    }
}
