(function () {

    var API_PREFIX = '/api/killrvideo/';

    var videoFactory = function ($http, $location, $log) {
        
        var factory = {};

        //Adds a new video
        factory.addVideo = function(user, video) {
            var videoJson = JSON.stringify(video);
            $log.log("Adding video " + videoJson);
            return $http.put(API_PREFIX+'videos/add/' + user,videoJson);
        }

        //Retrieves video information based on the specified video ID
        factory.deleteVideo = function(videoId) {
            return $http.get(API_PREFIX+'videos/delete/'+videoId);
        }

        //Retrieves video information based on the specified video ID
        factory.getVideo = function(videoId) {
            return $http.get(API_PREFIX+'videos/'+videoId);
        }

        //Retrieves a list of recent videos
        factory.getRecentVideos = function() {
            return $http.get(API_PREFIX+'videos/recent');
        }

        //Retrieves a list of all videos based on specified user ID
        factory.getUserVideos = function(userId) {
            return $http.get(API_PREFIX+'videos/user/'+userId);
        }

        //Retrieves a list of all videos based on a tag
        factory.getVideosByTag = function(tag) {
            return $http.get(API_PREFIX+'videos/tag/'+tag);
        }

        //Retrieves a list of all videos that matches any of the following tags
        factory.getVideosByTags = function(tags) {
            var tagsJson = JSON.stringify(tags);
            $log.log("Querying videos with the tags " + tagsJson);
            return $http.put(API_PREFIX+'videos/tags/',tagsJson);
        }

        return factory;
    };
    
    videoFactory.$inject = ['$http','$location','$log'];
    
    angular.module('killrVideoApp')
        .factory('videoFactory', videoFactory);

}());
