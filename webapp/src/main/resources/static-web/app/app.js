
(function () {
    'use strict';
    //Creates a d3 app which inserts a d3 tag into our page that
    //can be referenced later
    angular.module('d3',[])
        .factory('d3Service', ['$document','$q','$rootScope',
            function($document, $q, $rootScope) {

                var d = $q.defer();

                function onScriptLoad() {
                    $rootScope.$apply(function() { d.resolve(window.d3); });
                }
                // Create a script tag with d3 as the source
                var scriptTag = $document[0].createElement('script');
                scriptTag.type = 'text/javascript';
                scriptTag.async = true;
                scriptTag.src = 'http://d3js.org/d3.v3.min.js';
                scriptTag.onreadystatechange = function() {
                    if (this.readyState == 'complete') onScriptLoad();
                }
                scriptTag.onload = onScriptLoad;

                var s = $document[0].getElementsByTagName('body')[0];
                s.appendChild(scriptTag);

                return {
                    d3 : function() { return d.promise; }
                };
            }
        ]);

    var app = angular.module('killrVideoApp', ['ngRoute', 'ngCookies', 'ngAnimate', 'ngTagsInput', 'd3']);

    app.config(function ($routeProvider) {

        $routeProvider
            .when('/' || '/home', buildRoute('HomeController', 'app/views/home.html', true))
            .when('/login', buildRoute('AuthenticationController', 'app/views/login.html', false))
            .when('/register', buildRoute('RegistrationController', 'app/views/register.html', false))
            .when('/video/:videoId', buildRoute('VideoController', 'app/views/video.html', false))
            .when('/add', buildRoute('AddVideoController', 'app/views/addvideo.html', true))
            .when('/latest', buildRoute('LatestVideosController', 'app/views/latest.html', false))
            .when('/tag', buildRoute('TagController', 'app/views/tag.html', false))
            .when('/tag/:tag?', buildRoute('TagController', 'app/views/tag.html', false))
            .when('/videosearch', buildRoute('VideoSearchController', 'app/views/videosearch.html', false))
            .when('/videosearch/:queryField?/:query?', buildRoute('VideoSearchController', 'app/views/videosearch.html', false))
            .when('/logout', buildRoute('AuthenticationController', 'app/views/login.html', true))
            .when('/profile', buildRoute('HomeController', 'app/views/profile.html', true))
            .when('/oops', {
                templateUrl: 'app/views/error.html'
            })
            .otherwise({redirectTo: '/'});
    });

    app.filter('escape', function() {
        return window.encodeURIComponent;
    });

    //Little bit of a cludge here, adding parameters to the route object, probably a slicker way to do this but...
    function buildRouteWithParams(controller, templateUrl, authRequired, params) {
        var route = buildRoute(controller, templateUrl, authRequired)
        route.params = params;
        return route;
    }

    function buildRoute(_controller, _templateUrl, authRequired) {
        var route = {};
        route.templateUrl = _templateUrl;
        route.controller = _controller;
        if (authRequired) {
            route.resolve = { factory: checkAuth };
        }
        return route;
    }

    var checkAuth = function ($q, $rootScope, $location) {
        if ($rootScope.authtoken && $rootScope.loggedIn) {
            return true;
        } else {
            $q.reject();
            $location.url("/login");
        }
    };
}());

