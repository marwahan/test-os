angular.module('os.biospecimen.specimenlist', 
  [ 
    'os.biospecimen.specimenlist.name',
    'os.biospecimen.specimenlist.list',
    'os.biospecimen.specimenlist.addedit',
    'os.biospecimen.specimenlist.specimensholder',
    'os.biospecimen.specimenlist.assignto'
  ])

  .config(function($stateProvider) {
    $stateProvider
      .state('specimen-list', {
        url: '/specimen-list?listId',
        templateUrl: 'modules/biospecimen/specimen-list/list.html',
        controller: 'SpecimenListsCtrl',
        resolve: {
          reqBasedDistOrShip: function($injector) {
            if ($injector.has('spmnReqCfgUtil')) {
              return $injector.get('spmnReqCfgUtil').isReqBasedDistOrShippingEnabled();
            } else {
              return {value: false};
            }
          }
        },
        parent: 'signed-in'
      })
      .state('specimen-list-addedit', {
        url: '/specimen-list/:listId/addedit',
        templateUrl: 'modules/biospecimen/specimen-list/addedit.html',
        resolve: {
          list: function($stateParams, SpecimenList) {
            if ($stateParams.listId) {
              return SpecimenList.getById($stateParams.listId);
            }
            return new SpecimenList();
          }
        },
        controller: 'AddEditSpecimenListCtrl',
        parent: 'signed-in'
      });
  });


