(function() {

    var AddVideoController = function ($scope, $rootScope, $log, $location, $http, videoFactory) {

        //Takes a video object
        $scope.addvideo = function(video) {

            // Clean up the tags input since it's in the form text:<value>
            var temp = [];
            angular.forEach(video.tags, function(value, key) {
                angular.forEach(value, function(value, key) {
                    temp.push(value)
                })
            });
            video.tags = temp;

            videoFactory.addVideo($rootScope.user.userId, video)
                .success(function(data, status, headers, config) {
                    $log.log('Successfully added a video!');
                    $location.url('/home');
                })
                .error(function(data, status, headers, config) {
                    $log.log('Failed to add video...');
                    $location.url('/oops');

                });
        }

        //Converts image to byte array
        $scope.uploadImage = function (input) {

            if (input.files && input.files[0]) {
                var reader = new FileReader();
                reader.onload = function (e) {

                    //Sets the Old Image to new New Image
                    $('#photo-id').attr('src', e.target.result);

                    //Create a canvas and draw image on Client Side to get the byte[] equivalent
                    var canvas = document.createElement("canvas");
                    var imageElement = document.createElement("img");

                    imageElement.setAttribute('src', e.target.result);
                    canvas.width = imageElement.width;
                    canvas.height = imageElement.height;
                    var context = canvas.getContext("2d");
                    context.drawImage(imageElement, 0, 0);
                    var base64Image = canvas.toDataURL("image/jpeg");

                    //Removes the Data Type Prefix
                    //And set the view model to the new value
                    $scope.video.previewThumbnail = base64Image.replace(/data:image\/jpeg;base64,/g, '');
                }

                //Renders Image on Page
                reader.readAsDataURL(input.files[0]);
            }
        };

    };

    AddVideoController.$inject = ['$scope', '$rootScope', '$log', '$location', '$http', 'videoFactory'];

    angular.module('killrVideoApp')
      .controller('AddVideoController', AddVideoController);
    
}());