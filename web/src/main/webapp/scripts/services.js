/**
 *  Copyright 2016 Viktoriia Bakalova
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and limitations under the License.
 */

'use strict';

angular.module('compactProfiler')
    .constant("baseURL", "http://localhost:8080/")
    .service('profileService', ['$http', '$location', 'baseURL', function ($http, $location, baseURL) {

        var minimalProfile = "";
        var inDesiredProfile = [];
        var outsideDesiredProfile = [];
        var errorMessage = "";

        /**
         * Uploads form to server and performs appropriate redirects in case of success or error
         * @param files form data
         * @param path location on the server where to upload data
         */
        this.uploadFormToUrl = function (files, path){
            var fd = new FormData();
            fd.append('fileField', files[0]);
            fd.append('profileField', files[1]);
            $http.post(baseURL + path, fd, {
                transformRequest: angular.identity,
                headers: {'Content-Type': undefined}
            })
                .then(function (response) {
                    console.log(response);
                    minimalProfile = response.data.minimalProfile;
                    inDesiredProfile = response.data.inDesiredProfile;
                    outsideDesiredProfile = response.data.outsideDesiredProfile;
                    $location.path('/result');
                })
                .catch(function (response) {
                    errorMessage = "Error: " + response.status + " " + response.statusText + ". " + response.data.message;
                    $location.path('/error');
                });
        };

        this.getMinimalProfile = function () {
            return minimalProfile;
        };

        this.getInProfilePackages = function () {
            return inDesiredProfile;
        };

        this.getOutsideProfilePackages = function () {
            return outsideDesiredProfile;
        };

        this.getErrorMessage = function () {
            return errorMessage;
        };

    }]);

