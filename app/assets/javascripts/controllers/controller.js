define(["angular"], function (angular) {
    "use strict";

    return {
        ClientCtrl: function ($scope, playRoutes) {

            $scope.getImage = function () {

                playRoutes.controllers.Application.image().get()
                    .success(function (data, status, headers, config) {
                        $scope.currentImage = data.id
                    })
                    .error(function (data, status, headers, config) {
                        $scope.currentImage = "http://placehold.it/350x350&text=Currently+no+images+available"
                        $scope.errors.push("Retrieving a new image failed.")
                    });
                /*
                 $http({method: 'GET', url: clientServer + '/image'}).
                 success(function (data, status, headers, config) {
                 $scope.currentImage = data.id
                 }).
                 error(function (data, status, headers, config) {
                 $scope.currentImage = "http://placehold.it/350x350&text=Currently+no+images+available"
                 $scope.errors.push("Retrieving a new image failed.")
                 });
                 */
            }

            $scope.submitImage = function () {
                var postData = { tags: $scope.tags, id: $scope.currentImage}

                playRoutes.controllers.Application.submitImage().post(postData)
                    .success(function (data, status, headers, config) {
                        // $scope.users = data.users; // assign  $scope.persons here as promise is resolved here
                    }).error(function (data, status, headers, config) {
                        $scope.errors.push("Submit image failed.")
                    });
                /* $http({
                 url: '/image',
                 method: "POST",
                 data: postData,
                 headers: {'Content-Type': 'application/json'}
                 }).success(function (data, status, headers, config) {
                 // $scope.users = data.users; // assign  $scope.persons here as promise is resolved here
                 }).error(function (data, status, headers, config) {
                 $scope.errors.push("Submit image failed.")
                 });
                 */
            }


            $scope.addTag = function () {
                if ($scope.tag.length > 0) {
                    $scope.tags.push($scope.tag)
                    $scope.tag = ""
                }
            }

            $scope.removeTag = function (index) {
                console.log("removing index " + index)
                $scope.tags.splice(index, 1)
            }

            $scope.skip = function () {
                $scope.getImage()
                $scope.tags = []
                $scope.tag = ""
            }

            $scope.submit = function () {
                $scope.submitImage()
                $scope.workDone.push($scope.currentImage)
                $scope.getImage()
                $scope.tags = []
                $scope.tag = ""

            }


            $scope.tags = []

            $scope.tag = ""

            $scope.currentImage = $scope.getImage()

            $scope.workDone = []

            $scope.errors = []

        }
    }
});