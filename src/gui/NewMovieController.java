package gui;

import be.Movie;
import dal.MovieDAO;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class NewMovieController implements Initializable {
    public Button savebtn;
    public Button cancelNewMovie;
    public TextField titlelbl;
    public TextField lengthlbl;
    public ChoiceBox<String> imdb;
    public String[] imdbrating={"1","2","3","4","5","6","7","8","9","10"};
    public ChoiceBox<String> personalR;
    public String[] personalRating={"1","2","3","4","5","6","7","8","9","10"};
    public Button filechoosebtn;
    public TextField filelbl;
    public TextField categoryChoice;
    private Movie movieToUpdate;


    private MainScreenController m;



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        imdb.getItems().addAll(imdbrating);
        personalR.getItems().addAll(personalRating);


    }
    public void setMainScreenController(MainScreenController mainScreenController){
        this.m= mainScreenController;
    }

    //If the save button is clicked after user have updated some parameters of the movie it replaces the old mpovies with new.
    //If user was filling the fields for the first time it creates a new movie and adds it to the list.
    public void saveMovie(ActionEvent actionEvent) {
        MovieDAO movieDAO = new MovieDAO();
        if (movieToUpdate != null) {
            movieToUpdate.setMovieTitle(titlelbl.getText());
            movieToUpdate.setImdbRating(Integer.parseInt(imdb.getValue()));
            movieToUpdate.setPersRating(Integer.parseInt(personalR.getValue()));
            movieToUpdate.setMovieLength(Double.parseDouble(lengthlbl.getText()));
            movieToUpdate.setCategory(categoryChoice.getText());
            movieToUpdate.setFilepath(filelbl.getText());

            movieDAO.updateMovie(movieToUpdate);
            m.updateMovieInList(movieToUpdate);
        } else {
            Movie movie = new Movie();
            movie.setMovieTitle(titlelbl.getText());
            movie.setImdbRating(Integer.parseInt(imdb.getValue()));
            movie.setPersRating(Integer.parseInt(personalR.getValue()));
            movie.setMovieLength(Double.parseDouble(lengthlbl.getText()));
            movie.setCategory(categoryChoice.getText());
            movie.setFilepath(filelbl.getText());
            movieDAO.createMovie(movie);
            m.addMovie(movie);
            m.updateOriginalMovies();
        }

        // Close the stage
        Stage stage = (Stage) savebtn.getScene().getWindow();
        stage.close();
    }


    //Closes add or update movie window.
    public void cancelMovie(ActionEvent actionEvent) {
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }
    //This method extracts all filled fields of movie so that user
    // can see what parameters are at the moment and what he wants to change
    public void setMovieToUpdate(Movie movie) {
        this.movieToUpdate = movie;

        titlelbl.setText(movie.getMovieTitle());
        lengthlbl.setText(String.valueOf(movie.getMovieLength()));
        imdb.setValue(String.valueOf(movie.getImdbRating()));
        personalR.setValue(String.valueOf(movie.getPersRating()));
        categoryChoice.setText(movie.getCategory());
        filelbl.setText(movie.getFilepath());

    }

    public void chooseFile(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("MP4 Files","*.mp4","*.mpeg4")
        );
        Window SongClass = null;
        fileChooser.setInitialFileName(String.valueOf(new File("C:/Films")));
        File selectedfile = fileChooser.showOpenDialog(SongClass);
        if(selectedfile != null) {
            filelbl.setText(String.valueOf(selectedfile));
        }
        else{
            System.out.println("file is not valid");

        }
    }
}