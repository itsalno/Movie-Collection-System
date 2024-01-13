package gui;

import be.Category;
import be.Movie;
import dal.CategoryDAO;
import dal.MovieDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;


import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class MainScreenController implements Initializable {
    public Button closeapp;
    public TableView categoryTable;
    public TableView movieTable;
    public Button addcatbtn;
    public Button dltcatbtn;
    public Button updcatbtn;
    public Button addmoviebtn;
    public Button dltmoviebtn;
    public Button movieupd;
    public ListView movieListinCat;
    public Button movetocat;
    public Button playbtn;
    public TableColumn title;
    public TableColumn personalRating;
    public TableColumn imdb;
    public TableColumn category;
    public TableColumn file;

    public ObservableList<Movie> movieList;
    public ObservableList<Category> categoryList;
    public TableColumn genre;
    public TableColumn numberOfFilms;
    public TextField SearchBar;
    public Button SearchBtn;
    public Button resetTableBtn;
    private ObservableList<Movie> originalMovies;
    public ObservableList<Movie> selectedGenreMovies;


    private void setupOriginalMovies() {
        originalMovies = FXCollections.observableArrayList();
        originalMovies.addAll(movieList);
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // adding movies
        title.setCellValueFactory(new PropertyValueFactory<>("movieTitle"));
        personalRating.setCellValueFactory(new PropertyValueFactory<>("persRating"));
        imdb.setCellValueFactory(new PropertyValueFactory<>("imdbRating"));
        category.setCellValueFactory(new PropertyValueFactory<>("category"));
        file.setCellValueFactory(new PropertyValueFactory<>("filepath"));
        movieList = FXCollections.observableArrayList();
        movieTable.setItems(movieList);

        // adding categorys
        genre.setCellValueFactory(new PropertyValueFactory<>("name"));
        categoryList = FXCollections.observableArrayList();
        categoryTable.setItems(categoryList);
        //Saves Movies that are alredy in the table
       setupOriginalMovies();

       selectedGenreMovies = FXCollections.observableArrayList();


    }
    //Adds movie to the movie list.
    public void addMovie(Movie movie) {

        movieList.add(movie);
    }
    //Adds category to category list.
    public void addGenre(Category category) {
        categoryList.add(category);

    }

    //Closes the main window of application.
    public void closeApplication(ActionEvent actionEvent) {
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }
    //Opens new window for adding category.
    public void addCategory(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("NewCategory.fxml"));
        Parent root = loader.load();
        NewCategoryController newCategoryController = loader.getController();
        newCategoryController.setMainScreenController(this);
        Stage primaryStage = new Stage();
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
    //Deletes the category from the category table.
    public void deleteCategory(ActionEvent actionEvent) throws SQLException {
        Alert confirmationDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationDialog.setTitle("Confirmation");
        confirmationDialog.setHeaderText("Are you sure you want to delete this category?");
        confirmationDialog.setContentText("Any unsaved changes will be lost.");
        Optional<ButtonType> result = confirmationDialog.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            CategoryDAO CategoryDAO = new CategoryDAO();
            ObservableList<Category> allCategorys, singleCategory;
            allCategorys = categoryTable.getItems();
            singleCategory = categoryTable.getSelectionModel().getSelectedItems();
            System.out.println(singleCategory + "1");
            int id = CategoryDAO.getCatfromName(singleCategory.getFirst().getName());
            System.out.println(id + "2");
            CategoryDAO.deleteCategory(id);
            singleCategory.forEach(allCategorys::remove);
            categoryTable.refresh();
        }
    }
    //Opens up the update category window.
    public void updateCategory(ActionEvent actionEvent) throws IOException {
        Category selectedCategory = (Category) categoryTable.getSelectionModel().getSelectedItem();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("NewCategory.fxml"));
        Parent root = loader.load();

        NewCategoryController newCategoryController = loader.getController();

        newCategoryController.setMainScreenController(this);
        newCategoryController.setCategoryToUpdate(selectedCategory);

        Stage primaryStage = new Stage();
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
    //Opens a window for adding new movie.
    public void addMovie(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("NewMovie.fxml"));
        Parent root = loader.load();
        NewMovieController newMovieController = loader.getController();
        newMovieController.setMainScreenController(this);
        Stage primaryStage = new Stage();
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
    //Deletes movie from the movie table.
    public void deleteMovie(ActionEvent actionEvent) throws SQLException {
        Alert confirmationDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationDialog.setTitle("Confirmation");
        confirmationDialog.setHeaderText("Are you sure you want to delete this movie?");
        confirmationDialog.setContentText("Any unsaved changes will be lost.");
        Optional<ButtonType> result = confirmationDialog.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            MovieDAO MovieDAO = new MovieDAO();
            ObservableList<Movie> allMovies, singleMovie;
            allMovies = movieTable.getItems();
            singleMovie = movieTable.getSelectionModel().getSelectedItems();
            int id = MovieDAO.getMovfromName(singleMovie.getFirst().getMovieTitle());
            System.out.println(id + "2");
            MovieDAO.deleteMovie(id);
            singleMovie.forEach(allMovies::remove);
            movieTable.refresh();
        }
    }
    //Opens a window for updating a movie.
    public void updateMovie(ActionEvent actionEvent) throws IOException {
        Movie selectedMovie = (Movie) movieTable.getSelectionModel().getSelectedItem();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("NewMovie.fxml"));
        Parent root = loader.load();

        NewMovieController newMovieController = loader.getController();
        newMovieController.setMainScreenController(this);
        newMovieController.setMovieToUpdate(selectedMovie);

        Stage primaryStage = new Stage();
        primaryStage.setScene(new Scene(root));
        primaryStage.showAndWait();


        updateOriginalMovies();
    }

    public void moveToCategory(ActionEvent actionEvent) throws SQLException {
        Movie selectedMovie = (Movie) movieTable.getSelectionModel().getSelectedItem();
        Category selectedCategory = (Category) categoryTable.getSelectionModel().getSelectedItem();

        if (selectedMovie != null && selectedCategory != null) {
            CategoryDAO categoryDAO = new CategoryDAO();
            MovieDAO movieDAO = new MovieDAO();
            int cid = categoryDAO.getCatfromName(selectedCategory.getName());
            int mid = movieDAO.getMovfromName(selectedMovie.getMovieTitle());
            System.out.println(mid + "moviee");
            System.out.println(cid + "category");
            movieListinCat.getItems().add(selectedMovie.getMovieTitle());
            categoryDAO.addMovieToCategory(mid, cid);
        }
        /*Movie selectedMovie = (Movie) movieTable.getSelectionModel().getSelectedItem();
        Category selectedCategory = (Category) categoryTable.getSelectionModel().getSelectedItem();

        if(selectedMovie != null && selectedCategory != null){
            categoryTable.getItems().add(selectedMovie.getMovieTitle());
            CategoryDAO.addMovieToCategory(selectedMovie, selectedCategory);
        }*/
    }

    public void playMovie(ActionEvent actionEvent) {
        Movie selectedMovie = (Movie) movieTable.getSelectionModel().getSelectedItem();

        if (selectedMovie != null) {
            File movieFile = new File(selectedMovie.getFilepath());

            if (Desktop.isDesktopSupported()) {
                try {
                    Desktop.getDesktop().open(movieFile);
                } catch (IOException e) {
                    e.printStackTrace();
                    // Add additional logging or error handling if needed
                }
            } else {
                // Handle the case where Desktop is not supported
                System.out.println("Desktop is not supported. Unable to open the movie.");
            }
        } else {
            // Handle the case where no movie is selected
            System.out.println("No movie selected.");
        }
    }


    public void ClickSearchBtn(ActionEvent actionEvent) {
        String letter = SearchBar.getText().toUpperCase();
        List<Movie> allMovies = movieTable.getItems();

        // Create a new list for filtered movies
        ObservableList<Movie> filteredMovies = FXCollections.observableArrayList();

        for (Movie movie : allMovies) {
            if (movie.getMovieTitle().toUpperCase().contains(letter) ||
                    String.valueOf(movie.getMovieLength()).toUpperCase().contains(letter) ||
                    String.valueOf(movie.getPersRating()).toUpperCase().contains(letter) ||
                    String.valueOf(movie.getImdbRating()).toUpperCase().contains(letter) ||
                    movie.getCategory().toUpperCase().contains(letter)) {

                filteredMovies.add(movie);
            }
        }
        // Set the filtered movies to the movieTable
        movieTable.setItems(filteredMovies);
        if(SearchBar.getText().isEmpty()){
            // Reset the movieTable to the original state
            movieTable.setItems(originalMovies);
        }
    }
    //Updates the movies in the list after user updates the movie.
    public void updateMovieInList(Movie updatedMovie) {

        int index = movieList.indexOf(updatedMovie);

        if (index != -1) {
            movieList.set(index, updatedMovie);
        }
    }
    public void updateCategoryInList(Category updatedCategory){
        int index = categoryList.indexOf(updatedCategory);

        if (index != -1) {
            categoryList.set(index, updatedCategory);
        }
    }



    public void updateOriginalMovies() {
        originalMovies.clear();
        originalMovies.addAll(movieList);
    }

    public void showMoviesInCategoryList(ActionEvent actionEvent) throws IOException {
        Category selectedCategory = (Category) categoryTable.getSelectionModel().getSelectedItem();
        this.selectedGenreMovies.addAll(selectedCategory.getAllMovies());
    }
}



