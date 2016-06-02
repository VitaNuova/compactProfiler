/**
 *  Copyright 2016 Viktoriia Bakalova
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and limitations under the License.
 */

'use strict';

/**
 * compactProfiler module declaration and routing configuration
 */
angular.module('compactProfiler', ['ui.router'])
    .config(function ($stateProvider, $urlRouterProvider) {
        $stateProvider
            .state('app', {
                url: '/',
                views: {
                    'header' : {
                        templateUrl: 'views/header.html'
                    },
                    'content' : {
                        templateUrl: 'views/home.html',
                        controller: 'UploadFormController'
                    },
                    'footer' : {
                        templateUrl: 'views/footer.html'
                    }
                }
            })

            .state('app.about', {
                url: 'about',
                views: {
                    'content@': {
                        templateUrl: 'views/about.html'
                    }
                }
            })

            .state('app.result', {
                url: 'result',
                views: {
                    'content@': {
                        templateUrl: 'views/result.html',
                        controller: 'ResultController'
                    }
                }
            })

            .state('app.error', {
                url: 'error',
                views: {
                    'content@': {
                        templateUrl: 'views/404.html',
                        controller: 'ErrorController'
                    }
                }
            });

        $urlRouterProvider.otherwise('/');
    });