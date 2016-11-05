(function() {

    var TagController = function ($scope, $rootScope, $routeParams, $log, $location, $window, videoFactory, commonFactory) {

        var tag = $routeParams.tag;
        $scope.tag = tag;

        //Retrieves video info for multiple tags
        $scope.getVideosByTags = function(tags) {

            // Clean up the tags input since it's in the form text:<value>
            var temp = [];
            angular.forEach(tags, function(value, key) {
                angular.forEach(value, function(value, key) {
                    temp.push(value)
                })
            });
            tags = temp;

            videoFactory.getVideosByTags(tags)
                .success(setResults)
                .error(function(data,status,headers, config) {
                    $log.log("Querying tags failed")
                });
         }

         function setResults(videos) {
            $scope.videos = videos;
         }

        //Retrieves video info
        function init(tag) {
            $log.log("Retrieving videos for " + tag);
            videoFactory.getVideosByTag(tag)
                .success(function(videos) {
                    $log.log("Videos tagged with "+ tag +" retrieved successfully");
                    $scope.videos = videos;
                })
                .error(commonFactory.evaluateError);
        }

        //If a tag has been specified then perform a query
        if(tag != undefined) {
            init(tag);
        }
    };

    TagController.$inject = ['$scope', '$rootScope', '$routeParams', '$log', '$location', '$window', 'videoFactory', 'commonFactory'];

    angular.module('killrVideoApp').controller('TagController', TagController);

}());