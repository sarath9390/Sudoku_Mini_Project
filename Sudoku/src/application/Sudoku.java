package application;

import java.util.Random;
import java.util.function.UnaryOperator;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import javafx.scene.paint.Color;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.scene.shape.Rectangle;



 
public class Sudoku extends Application {
	private int[][] solvedBoard=new int[9][9];
	private int[][] board=new int[9][9];
	private int[][] tempBoard=new int[9][9];
	private int[][] solvedTestBoard=new int[9][9];
    public static void main(String[] args) {
        launch(args);
    }
    
    
    boolean checkForResult(int row,int col,int value) {
    	for(int i=0;i<9;i++) {
    	
			if(solvedBoard[row][i]==value)
				return false;
    		
    		
			if(solvedBoard[i][col]==value)
				return false;
		
		
			if(solvedBoard[3*(row/3)+ i/3][3*(col/3)+ i%3] == value)
				return false;
    		
    	}
    	return true;
    	
    }
    
    private boolean solveSudoku(int row, int col) {
      
        if (row == 8 && col == 9) {
            return true;
        }
        
        if (col == 9) {
            row++;
            col = 0;
        }
        if (solvedBoard[row][col] != 0) {
            return solveSudoku(row, col + 1);
        }
        for (int num = 1; num <= 9; num++) {
            if (checkForResult(row, col, num)) {
            	solvedBoard[row][col] = num;
                if (solveSudoku(row, col + 1)) {
                    return true;
                }
                solvedBoard[row][col] = 0; 
            }
        }
        return false;
    }
    
    int[][] removeNumbers(int num) {
    	Random r=new Random();
    	int puzzle[][]=solvedBoard;
    	for(int i=0;i<num;) {
    		int row=r.nextInt(9);
    		int col=r.nextInt(9);
    		if(puzzle[row][col] != 0) {
    			puzzle[row][col]=0;
    			i++;
    		}
    	}
    	return puzzle;
    }
    
    private GridPane generateSudokuHelper() {
    	Random r=new Random();
		GridPane gp=new GridPane();
		solveSudoku(0,0);
		board=removeNumbers(40);
		tempBoard=board;
		
		for(int i=0;i<9;i++) {
			for(int j=0;j<9;j++) {
				if(board[i][j]==0) {
					TextField tf=new TextField();
					tf.setAlignment(Pos.CENTER);
					tf.setStyle("-fx-font-size: 18px;");
					tf.setPrefSize(50, 50);
					UnaryOperator<TextFormatter.Change> filter = change -> {
	                    String newText = change.getControlNewText();
	                    if (newText.matches("\\d*")) {
	                        return change;
	                    }
	                    return null;
	                };
	                TextFormatter<String> formatter = new TextFormatter<>(filter);
	                tf.setTextFormatter(formatter);
					tf.textProperty().addListener((obs, oldText, newText) -> {
	                    if (newText.length() > 1) { // Limit to 1 character
	                        tf.setText(newText.substring(0, 1));
	                    }
	                });
					gp.add(tf, j, i);
				}
				else {
					TextField tf=new TextField();
					tf.setPrefSize(50, 50);
					tf.setAlignment(Pos.CENTER);
					tf.setStyle("-fx-font-size: 18px;");
					String str=Integer.toString(board[i][j]);
					tf.setText(str);
					tf.setEditable(false);
					gp.add(tf, j, i);
					
				}
			}
		}
		
		return gp;
		
	}
    
    boolean valresult(int row,int col,int value) {
    	for(int i=0;i<9;i++) {
    	   if(col != i)
			if(solvedBoard[row][i]==value)
				return false;
    		
    		if(row != i)
			if(solvedBoard[i][col]==value)
				return false;
		
    		
		if(3*(row/3)+ i/3 != row && 3*(col/3)+ i%3 != col)
			if(solvedBoard[3*(row/3)+ i/3][3*(col/3)+ i%3] == value)
				return false;
    		
    	}
    	return true;
    	
    }
    
    private boolean validate(GridPane gp,Label l) {
    	//boolean flag=false;
    	for(int i=0;i<9;i++) {
    		for(int j=0;j<9;j++) {
    			if(!valresult(i,j,tempBoard[i][j])) {
    				return false;
    			}
    		}
    		
    	}
    	return true;
    	
    }
    
    private javafx.scene.Node getNodeByRowColumnIndex(int row, int column, GridPane gridPane) {
        for (javafx.scene.Node node : gridPane.getChildren()) {
            if (GridPane.getRowIndex(node) != null && GridPane.getRowIndex(node) == row &&
                GridPane.getColumnIndex(node) != null && GridPane.getColumnIndex(node) == column) {
                return node;
            }
        }
        return null;
    }
    
    private boolean checkFilled(GridPane gp,Label l) {
    	for(int i=0;i<9;i++) {
    		for(int j=0;j<9;j++) {
    			TextField tf=(TextField)getNodeByRowColumnIndex(i,j,gp);
    			if(tf==null || tf.getText().trim().isEmpty()) {
    				l.setText("Please fill all the cells");
    				tempBoard=board;
    				return false;
    			}
    			String str=tf.getText().trim();
    			int n=Integer.parseInt(str);
    			tempBoard[i][j]=n;
    		}
    	}
    	return true;
    	
    }
    
    
    private void generateSudoku(Stage pt) {
    	GridPane gp=generateSudokuHelper();
//    	gp.setHgap(0);
//    	gp.setVgap(0);
    	//gp.setStyle("-fx-border-color: black; -fx-border-width: 2px; -fx-border-style: solid;");
    	gp.setAlignment(Pos.CENTER);
    	for(int i=0;i<9;i=i+3) {
    		for(int j=0;j<9;j=j+3) {
    			Rectangle r=new Rectangle(150,150);
    			r.setStroke(Color.BLACK);
    			r.setFill(null);
    			r.setStrokeWidth(2);
    			gp.add(r, j, i,3,3);
    			
    		}
    	}
    	Label l=new Label(" ");
    	Button valid=new Button("Validate");
    	Button clear=new Button("clear");
    	valid.setPrefSize(100, 50);
    	clear.setPrefSize(100, 50);
    	valid.setAlignment(Pos.CENTER);
    	clear.setAlignment(Pos.CENTER);
        VBox v=new VBox(100);
        v.setAlignment(Pos.CENTER);
        HBox h=new HBox();
        h.setAlignment(Pos.CENTER);
        h.setSpacing(25);
        h.getChildren().addAll(valid,clear);
        v.getChildren().addAll(gp,l,h);
       valid.setOnAction(new EventHandler<ActionEvent>(){
    	   public void handle(ActionEvent ae) {
    		   if(checkFilled(gp,l)) {
    			   if(validate(gp,l)) {
    				   l.setText("You are Genius....Congracts");
    			   }
    			   else
    				   l.setText("Try again");
    		   }
    		   
    	   }
       });
       clear.setOnAction(new EventHandler<ActionEvent>(){
    	   public void handle(ActionEvent ae) {
    		   for (javafx.scene.Node node : gp.getChildren()) {
    		        if (node instanceof TextField) {
    		            TextField tf = (TextField) node;
    		            if (tf.isEditable()) {
    		                tf.clear(); // Clear the text field
    		            }
    		        }
    		    }
    		   
    	   }
       });
        
    	Scene s=new Scene(v,300,300);
    	pt.setScene(s);
    	pt.show();
    }
    
    private GridPane generateBoard() {
    	GridPane gp=new GridPane();
    	for(int i=0;i<9;i++) {
    		for(int j=0;j<9;j++) {
    			TextField tf=new TextField();
    			tf.setAlignment(Pos.CENTER);
				tf.setStyle("-fx-font-size: 18px;");
				tf.setPrefSize(50, 50);
				UnaryOperator<TextFormatter.Change> filter = change -> {
                    String newText = change.getControlNewText();
                    if (newText.matches("\\d*")) {
                        return change;
                    }
                    return null;
                };
                TextFormatter<String> formatter = new TextFormatter<>(filter);
                tf.setTextFormatter(formatter);
				tf.textProperty().addListener((obs, oldText, newText) -> {
                    if (newText.length() > 1) { // Limit to 1 character
                        tf.setText(newText.substring(0, 1));
                    }
                });
				gp.add(tf, j, i);
    		}
    	}
    	return gp;
    }
    
    private int[][] getValues(GridPane gp){
    	int[][] values=new int[9][9];
    	for(int i=0;i<9;i++) {
    		for(int j=0;j<9;j++) {
    			TextField tf=(TextField)getNodeByRowColumnIndex(i,j,gp);
    			if(tf==null || tf.getText().trim().isEmpty()) {
    				continue;
    			}
    			String str=tf.getText().trim();
    			int n=Integer.parseInt(str);
    			values[i][j]=n;
    		}
    	}
    	return values;
    }
    boolean checkForTestResult(int row,int col,int value) {
    	for(int i=0;i<9;i++) {
    	
			if(solvedTestBoard[row][i]==value)
				return false;
    		
    		
			if(solvedTestBoard[i][col]==value)
				return false;
		
		
			if(solvedTestBoard[3*(row/3)+ i/3][3*(col/3)+ i%3] == value)
				return false;
    		
    	}
    	return true;
    	
    }

    private boolean solveTestSudoku(int row, int col) {
        
        if (row == 8 && col == 9) {
            return true;
        }
        
        if (col == 9) {
            row++;
            col = 0;
        }
        if (solvedTestBoard[row][col] != 0) {
            return solveTestSudoku(row, col + 1);
        }
        for (int num = 1; num <= 9; num++) {
            if (checkForTestResult(row, col, num)) {
            	solvedTestBoard[row][col] = num;
                if (solveTestSudoku(row, col + 1)) {
                    return true;
                }
                solvedTestBoard[row][col] = 0; 
            }
        }
        return false;
    }
    
    private boolean checkTestFilled(GridPane gp,Label l) {
    	int c=0;
    	for(int i=0;i<9;i++) {
    		for(int j=0;j<9;j++) {
    			TextField tf=(TextField)getNodeByRowColumnIndex(i,j,gp);
    			if(tf==null || tf.getText().trim().isEmpty()) {
    				continue;
    			}
    			else
    				c++;
    		}
    	}
    	if(c>=35) {
    		return true;
    	}
    	l.setText("Please fill atleast 35 cells");
    	return false;
    	
    }
    
    private void generateSudokuForTest(Stage pt) {
    	GridPane gp=generateBoard();
    	Button solve=new Button("solve");
    	Label l=new Label();
    	solve.setPrefSize(100, 50);
    	solve.setAlignment(Pos.CENTER);
    	gp.setAlignment(Pos.CENTER);
    	for(int i=0;i<9;i=i+3) {
    		for(int j=0;j<9;j=j+3) {
    			Rectangle r=new Rectangle(150,150);
    			r.setStroke(Color.BLACK);
    			r.setFill(null);
    			r.setStrokeWidth(2);
    			gp.add(r, j, i,3,3);
    			
    		}
    	}
    	solve.setOnAction(new EventHandler<ActionEvent>() {
    		public void handle(ActionEvent ae) {
    			solvedTestBoard=getValues(gp);
//    			for(int i=0;i<9;i++) {
//    				for(int j=0;j<9;j++) {
//    					System.out.print(solvedTestBoard[i][j]);
//    				}
//    				System.out.println();
//    			}
    			
    			if(checkTestFilled(gp,l)) {
    				if(solveTestSudoku(0,0)) {
    					for(int i=0;i<9;i++) {
	    					for(int j=0;j<9;j++) {
	    						TextField tf = (TextField) getNodeByRowColumnIndex(i, j, gp);
	                            tf.setText(Integer.toString(solvedTestBoard[i][j]));
	    					}
	    				}
    				 }
    				else {
    					l.setText("Not possible");
    				}
    			
    			}
    			
    			
    		}
    	});
    	VBox v=new VBox(100);
    	v.setAlignment(Pos.CENTER);
    	v.getChildren().addAll(gp,l,solve);
    	v.setSpacing(100);
    	Scene s=new Scene(v,300,300);
    	pt.setScene(s);
    	pt.show();
    	
    	
    }
    
    @Override
    public void start(Stage pt) {
        pt.setTitle("Sudoku");
        Button b1=new Button("Play");
        Button b2=new Button("Test");
        b1.setPrefSize(50, 50);
        b2.setPrefSize(50, 50);
        HBox hb=new HBox(b1,b2);
        hb.setSpacing(25);
        b1.setOnAction(new EventHandler<ActionEvent>() {
        	public void handle(ActionEvent ae) {
        		generateSudoku(pt);
        	}
        });
        b2.setOnAction(new EventHandler<ActionEvent>() {
        	public void handle(ActionEvent ae) {
        		generateSudokuForTest(pt);
        	}
        });
        
        
        hb.setAlignment(Pos.CENTER);
        pt.setScene(new Scene(hb, 300, 250));
        pt.show();
    }
}
