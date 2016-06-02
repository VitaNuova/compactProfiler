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
    .constant('post', 'app/profiler/jarupload')
    .controller('ResultController', ['$scope', '$filter', 'profileService', function ($scope, $filter, profileService) {

        var inProfilePackages = profileService.getInProfilePackages();
        var outsideProfilePackages = profileService.getOutsideProfilePackages();
        var previousInProfile = [];
        var previousOutProfile = [];

        $scope.tab = 1;
        $scope.filterText = "";
        $scope.minimalProfile = profileService.getMinimalProfile();

        /**
         * Switches between tabs in the result view
         * @param setTab tab to choose
         */
        $scope.select = function(setTab) {
            $scope.tab = setTab;
        };

        /**
         * Checks whether tab is selected
         * @param checkTab tab to check
         * @returns {boolean} true if tab is selected, false otherwise
         */
        $scope.isSelected = function(checkTab) {
            return($scope.tab === checkTab);
        };

        $scope.inProfilePackages = inProfilePackages;
        $scope.outsideProfilePackages = outsideProfilePackages;

        /**
         * Filters package names according to the filter text specified
         */
        $scope.filterValues = function() {
            if ($scope.isSelected(1)) {
                $scope.inProfilePackages = $filter('filter')($scope.inProfilePackages, $scope.filterText);
            } else {
                $scope.outsideProfilePackages = $filter('filter')($scope.outsideProfilePackages, $scope.filterText);
            }
        };

        /**
         * Resets result view to its original state
         */
        $scope.resetAll = function() {
            $scope.inProfilePackages = inProfilePackages;
            $scope.outsideProfilePackages = outsideProfilePackages;
        };

        $scope.alphabeticSort = false;
        /**
         * Sorts packages alphabetically or resets to previous state if already sorted
         */
        $scope.toggleAlphabetic = function() {
            $scope.alphabeticSort = !$scope.alphabeticSort;
            if ($scope.profileSort) {
                $scope.profileSort = false;
            }
            if ($scope.alphabeticSort) {
                if ($scope.isSelected(1)) {
                    previousInProfile = $scope.inProfilePackages;
                    $scope.inProfilePackages = $filter('orderBy')($scope.inProfilePackages, 'packageName');
                } else {
                    previousOutProfile = $scope.outsideProfilePackages;
                    $scope.outsideProfilePackages = $filter('orderBy')($scope.outsideProfilePackages, 'packageName');
                }
            } else {
                if ($scope.isSelected(1)) {
                    $scope.inProfilePackages = previousInProfile;
                } else {
                    $scope.outsideProfilePackages = previousOutProfile;
                }
            }
        };

        $scope.profileSort = false;
        /**
         * Sorts packages by profile or resets to previous if already sorted
         */
        $scope.toggleProfile = function() {
            $scope.profileSort = !$scope.profileSort;
            if ($scope.alphabeticSort) {
                $scope.alphabeticSort = false;
            }
            if ($scope.profileSort) {
                if ($scope.isSelected(1)) {
                    previousInProfile = $scope.inProfilePackages;
                    $scope.inProfilePackages = $filter('orderBy')($scope.inProfilePackages, 'profileName');
                } else {
                    previousOutProfile = $scope.outsideProfilePackages;
                    $scope.outsideProfilePackages = $filter('orderBy')($scope.outsideProfilePackages, 'profileName');
                }
            } else {
                if ($scope.isSelected(1)) {
                    $scope.inProfilePackages = previousInProfile;
                } else {
                    $scope.outsideProfilePackages = previousOutProfile;
                }
            }
        };

    }])

    .controller('UploadFormController', ['$scope', 'post', 'profileService', function ($scope, post, profileService) {
        $(document).on('change', '.btn-file :file', function () {
            $(this).trigger('fileselect', [$(this).val()]);
        });

        $(document).ready(function() {
            $('.btn-file :file').on('fileselect', function (event, label) {
                var input = $(this).parents('.input-group').find(':text');
                input.val(label);
            });
        });

        $scope.profile = "";

        /**
         * Submits form to the server and resets its to pristine state
         */
        $scope.submit = function () {
            var files = [];
            files.push($scope.myFile);
            files.push($scope.profile);
            profileService.uploadFormToUrl(files, post);
            $scope.fileUploadForm.$setPristine();
        };

    }])

    .controller('NavbarController', ['$scope', function($scope) {
        $scope.tab = 'Home';

        /**
         * Switches to selected tab in the navigation bar
         * @param setTab tab to select
         */
        $scope.select = function (setTab) {
            $scope.tab = setTab;
        };

        /**
         * Checks if tab is selected
         * @param checkTab tab to check
         * @returns {boolean} true if tab is selected, false otherwise
         */
        $scope.isSelected = function (checkTab) {
            return ($scope.tab === checkTab);
        };
    }])

    .controller('ErrorController', ['$scope', 'profileService', function($scope, profileService) {
        $scope.message = profileService.getErrorMessage();
    }]);