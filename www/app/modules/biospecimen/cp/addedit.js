
angular.module('os.biospecimen.cp.addedit', ['os.biospecimen.models', 'os.administrative.models'])
  .controller('CpAddEditCtrl', function(
    $scope, $state, $stateParams, cp, extensionCtxt, User, Site, ExtensionsUtil, PvManager) {

    function init() {
      $scope.cp = cp;
      $scope.deFormCtrl = {};
      $scope.extnOpts = ExtensionsUtil.getExtnOpts(cp, extensionCtxt);
      $scope.coordinators = [];

      if (!cp.spmnLabelPrintSettings || cp.spmnLabelPrintSettings.length == 0) {
        $scope.cp.spmnLabelPrintSettings = [
          {"lineage": "New"},
          {"lineage": "Derived"},
          {"lineage": "Aliquot"}
        ];
      }

      loadPvs();

      if (!!cp.id && $stateParams.mode == 'copy') {
        $scope.mode = 'copy';
        $scope.copyFrom = cp.id;
        angular.forEach(cp.cpSites, function(site) {
          delete site.id;
        });
        delete cp.id;
        cp.title = cp.shortTitle = cp.code = "";
      }
    };

    function loadPvs() {
      $scope.sites = [];
      var op = !$scope.cp.id ? 'Create' : 'Update';
      var opts = {resource:'CollectionProtocol', operation: op};
      Site.query(opts).then(function(sites) {
         angular.forEach(sites, function(site) {
           $scope.sites.push(site.name);
         })
         
         $scope.cp.repositoryNames = cp.getRepositoryNames();
      });

      $scope.spmnLabelPrePrintModes = PvManager.getPvs('specimen-label-pre-print-modes');
      loadLabelAutoPrintModes();
    }

    function loadLabelAutoPrintModes() {
      $scope.spmnLabelAutoPrintModes = [];

      PvManager.loadPvs('specimen-label-auto-print-modes').then(
        function(pvs) {
          if ($scope.cp.spmnLabelPrePrintMode != 'NONE') {
            $scope.spmnLabelAutoPrintModes = pvs;
          } else {
            $scope.spmnLabelAutoPrintModes = pvs.filter(
      	      function(pv) {
      	        return pv.name != 'PRE_PRINT';
      	      }
      	    );
          }
        }
      );
    }

    $scope.createCp = function() {
      var formCtrl = $scope.deFormCtrl.ctrl;
      if (formCtrl && !formCtrl.validate()) {
        return;
      }

      var cp = angular.copy($scope.cp);
      delete cp.repositoryNames;

      if (formCtrl) {
        cp.extensionDetail = formCtrl.getFormData();
      }

      var q;
      if ($scope.mode == 'copy') {
        q = cp.copy($scope.copyFrom);
      } else {
        q = cp.$saveOrUpdate();
      }

      q.then(
        function(savedCp) {
          $state.go('cp-detail.overview', {cpId: savedCp.id});
        }
      );
    };

    $scope.onRepositorySelect = function(repositoryName) {
      if (!$scope.cp.cpSites) {
        $scope.cp.cpSites = [];
      }
      $scope.cp.cpSites.push({siteName: repositoryName, code: undefined});
    }

    $scope.onRepositoryRemove = function(repositoryName) {
      var sites = $scope.cp.cpSites;
      for (var i = 0; i < sites.length; i++) {
        if (sites[i].siteName == repositoryName) {
          $scope.cp.cpSites.splice(i, 1);
          break;
        }
      }
    }

    $scope.onPrePrintModeChange = function() {
      $scope.prePrintDisabled = !!$scope.cp.id && $scope.cp.spmnLabelPrePrintMode == 'NONE';

      loadLabelAutoPrintModes();
      if ($scope.prePrintDisabled) {
        angular.forEach($scope.cp.spmnLabelPrintSettings, function(setting) {
          if (setting.printMode == 'PRE_PRINT') {
            setting.printMode = '';
          }
        });
      }
    }

    init();
  });
