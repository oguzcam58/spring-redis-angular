app = angular.module("libraryApp", ["ui.bootstrap"]);

// Main controller runs when index html is loaded
app.controller("IndexCtrl", function($scope, $modal, getAllBooks, commonFactory) {
  $scope.alerts = [];
	$scope.books = [];
  $scope.newBook = {id:0, name:"", author:""};

  var getBookPromise = getAllBooks.getData();
  getBookPromise.then(function(result) {  // this is only run after $http completes
    if(result.type == "Success"){
      $scope.books = result.books;
    } else if (result.type == "Fail"){
      $scope.alerts.push({"type":"danger",
                          "msg":result.message});
    } else {
      $scope.alerts.push(commonFactory.getServerErrorMessage());
    }
  });

  // Close read alert
	$scope.closeAlert = function(index) {
   	$scope.alerts.splice(index, 1);
  };

  // Delete a book
  $scope.delete = function(title, book){
  	var deleteModalInstance = $modal.open({
      animation: true,
      templateUrl: 'deleteBookModal.html',
      controller: 'DeleteCtrl',
      resolve: {
        getItems: function () {
          items = {"title":title,
                   "book":book}
          return items;
        }
      }
    });

    deleteModalInstance.result.then(function (result) {
      $scope.alerts = [];
      $scope.alerts.push({"type":"success",
                          "msg": book.name + " has been deleted successfully"});
      // To refresh main list
      var getBookPromise = getAllBooks.getData();
      getBookPromise.then(function(result) {  // this is only run after $http completes
        if(result.type == "Success"){
          $scope.books = result.books;
        } else if (result.type == "Fail"){
          $scope.alerts.push({"type":"danger",
                              "msg":result.message});
        } else {
          $scope.alerts.push(commonFactory.getServerErrorMessage());
        }
      });
    }, function () {

    });
  };

  // Create or update a book
  $scope.open = function (title, book) {
    var saveModalInstance = $modal.open({
      animation: true,
      templateUrl: 'createUpdateBookModal.html',
      controller: 'CreateUpdateCtrl',
      resolve: {
        getItems: function () {
          items = {"title":title,
                   "book":book}
          return items;
        }
      }
    });

    saveModalInstance.result.then(function (result) {
      $scope.alerts = [];
      $scope.alerts.push({"type":"success",
                          "msg":result.message});
      // To refresh main list
      var getBookPromise = getAllBooks.getData();
      getBookPromise.then(function(result) {  // this is only run after $http completes
        if(result.type == "Success"){
          $scope.books = result.books;
        } else if (result.type == "Fail"){
          $scope.alerts.push({"type":"danger",
                              "msg":result.message});
        } else {
          $scope.alerts.push(commonFactory.getServerErrorMessage());
        }
      });

    }, function () {

    });
  };
});

// CreateUpdate Modal Dialog
app.controller('CreateUpdateCtrl', function ($scope, $modalInstance, getItems, saveBook) {
  $scope.alerts = [];
  $scope.title = getItems.title;
  $scope.book = getItems.book;

  $scope.ok = function () {
    $scope.alerts = [];

    // Validate data
    if($scope.book.name == "" || $scope.book.author == "" ||
       $scope.book.name == null || $scope.book.author == null) {
      $scope.alerts.push({"type":"danger",
                          "msg":"Book's info should be provided."});
    } else {
      var saveBookPromise = saveBook.getData($scope.book);
      saveBookPromise.then(function(result) {  // this is only run after $http completes
        if(result.type == "Success"){
          $scope.books = result.books;
          $modalInstance.close(result);
        } else if (result.type == "Fail"){
          $scope.alerts.push({"type":"danger",
                              "msg":result.message});
        } else {
          $scope.alerts.push(commonFactory.getServerErrorMessage());
        }
      });
    }
  };

  $scope.cancel = function () {
    $modalInstance.dismiss('cancel');
  };

  $scope.closeAlert = function(index) {
    $scope.alerts.splice(index, 1);
  };
});

// Delete Modal Dialog
app.controller('DeleteCtrl', function ($scope, $modalInstance, getItems, deleteBook) {

  $scope.title = getItems.title;
  $scope.book = getItems.book;

  $scope.ok = function () {
    var deleteBookPromise = deleteBook.getData($scope.book.id);
    deleteBookPromise.then(function(result) {  // this is only run after $http completes
      if(result.type == "Success"){
        $modalInstance.close(result.message);
      } else if (result.type == "Fail"){
        $scope.alerts.push({"type":"danger",
                            "msg":result.message});
      } else {
        $scope.alerts.push(commonFactory.getServerErrorMessage());
      }
    });
  };

  $scope.cancel = function () {
    $modalInstance.dismiss('cancel');
  };
});

// This factory gets all books from RestFul getAllBooks Service
app.factory("getAllBooks", function($http, commonFactory) {
  var getData = function() {
    showLoadingGif();
    return $http.get(commonFactory.getBackendUrl() + "/getAllBooks").then(function(result){
      hideLoadingGif();
      return result.data;
    });
    hideLoadingGif();
  };
  return { getData: getData };
});

// This factory creates or updates a book via RestFul saveBook Service
app.factory("saveBook", function($http, commonFactory) {
  var getData = function(book) {
    showLoadingGif();
    return $http.get(commonFactory.getBackendUrl() + "/saveBook?book=" + JSON.stringify(book)).then(function(result){
      hideLoadingGif();
      return result.data;
    });
    hideLoadingGif();
  };
  return { getData: getData };
});

// This factory creates a new book via RestFul createBook Service
app.factory("deleteBook", function($http, commonFactory) {
  var getData = function(bookId) {
    showLoadingGif();
    return $http.get(commonFactory.getBackendUrl() + "/deleteBook?bookId=" + bookId).then(function(result){
      hideLoadingGif();
      return result.data;
    });
    hideLoadingGif();
  };
  return { getData: getData };
});

app.factory("commonFactory", function(){
  var commonFactory = {};
  var backendUrl = "http://localhost:8080";
  var serverErrorMessage = {"type":"danger",
                            "msg":"Server is not available at this moment, please try again later!!!"};

  commonFactory.getBackendUrl = function(){
    return backendUrl;
  };

  commonFactory.getServerErrorMessage = function(){
    return serverErrorMessage;
  };

  return commonFactory;
})

var showLoadingGif = function() {
  $(".loading-background").show();
  $("#loading-text").show();
};

var hideLoadingGif = function() {
  $(".loading-background").hide();
  $("#loading-text").hide();
};