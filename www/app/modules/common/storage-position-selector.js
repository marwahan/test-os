
angular.module('openspecimen')
  .controller('StoragePositionSelectorCtrl',
    function($scope, $modalInstance, $timeout, $q, entity, cpId, Container) {
      function init() {
        $scope.listOpts = { 
          type: entity.getType(),
          name: !!entity.storageLocation ? entity.storageLocation.name : ''
        };

        if (entity.getType() == 'specimen') {
          $scope.listOpts.criteria = {
            storeSpecimensEnabled: true,
            specimenClass: entity.specimenClass,
            specimenType: entity.type,
            cpId: cpId
          }
        } else {
          $scope.listOpts.criteria = { site: entity.siteName };
        }

        $scope.selectedContainer = {}; // step 1
        $scope.selectedPos = {};       // step 2
        $scope.showGrid = false;       // when to draw and show occupancy grid
        $scope.entity = entity;        // occupying entity for which slot is being selected
        $scope.cpId = cpId;
      };

      $scope.toggleContainerSelection = function(wizard, container) {
        $scope.showGrid = false;
        if (container.selected) {
          $scope.selectedPos = {id: container.id, name: container.name};
          $scope.selectedContainer = container;
          wizard.next(false);
        } else {
          $scope.selectedPos = {};
          $scope.selectedContainer = {};
        }
      }

      $scope.getOccupancyMap = function() {
        if ($scope.selectedContainer.occupiedPositions) {
          // occupied positions map already loaded
          $scope.showGrid = true;
          return true;
        }

        return Container.getById($scope.selectedContainer.id).then(
          function(container) {
            angular.extend($scope.selectedContainer, container);
            $scope.showGrid = true;
            return true;
          }
        );
      };

      $scope.cancel = function() {
        $modalInstance.dismiss('cancel');
      };

      $scope.ok = function() {
        if (!$scope.selectedContainer || !$scope.selectedContainer.id) {
          $scope.cancel();
        } else {
          $modalInstance.close($scope.selectedPos);
        }
      };
             
      init();
    }
  );
