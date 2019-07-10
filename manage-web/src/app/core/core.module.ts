import {NgModule} from '@angular/core';
import {HTTP_INTERCEPTORS, HttpClientModule} from '@angular/common/http';
import {NgZorroAntdModule} from 'ng-zorro-antd';

import {HttpUtilService} from './infra/http/http-util.service';

import {DefaultInterceptor, UfastCodeInterceptor} from './infra/interceptors/default.interceptor';
import {UfastValidatorsService} from './infra/validators/validators.service';
import {UfastValidatorsRuleService} from './infra/validators/validatorsRule.service';

import {UserService} from './common-services/user.service';
import {MenuService} from './common-services/menu.service';
import {ScepterService} from './common-services/scepter.service';
import {DeptService} from './common-services/dept.service';
import {DictionaryService} from '../core/common-services/dictionary.service';


import {RouteReuseStrategy} from '@angular/router';
import {UfastReuseStrategy, UfastTabsetRouteService} from './infra/ufast-tabset-route.service';
import {NewsService} from './common-services/news.service';
import {NavigationService} from './trans/navigation.service';
import {IndexpicService} from './trans/indexpic.service';
import {UfastUtilService} from './infra/ufast-util.service';
import {LodopPrintService} from './infra/lodop-print.service';
import {PrintService} from './trans/print.service';


import {WorkBoardService} from './trans/work-board.service';
import {AuthService} from './common-services/auth.service';
import {UserLeaveService} from './trans/internal/user-leave.service';
import {RoleService} from './common-services/role.service';
import {GoodsCategoryService} from './common-services/goods-category.service';
import {GoodsAttributeService} from './common-services/goods-attribute.service';
import {GoodsService} from './common-services/goods.service';
import {WarehouseService} from "./trans/warehouse.service";

/**
 * 定义拦截器顺序，
 * 参考：https://angular.cn/guide/http#interceptor-order
 **/
const httpInterceptorProvider = [
  {provide: HTTP_INTERCEPTORS, useClass: DefaultInterceptor, multi: true},
  {provide: HTTP_INTERCEPTORS, useClass: UfastCodeInterceptor, multi: true}
];


@NgModule({
  imports: [
    HttpClientModule,
    NgZorroAntdModule
  ],
  providers: [
    {provide: RouteReuseStrategy, useClass: UfastReuseStrategy},
    HttpUtilService,
    httpInterceptorProvider,
    UserService,
    MenuService,
    ScepterService,
    DeptService,
    NewsService,
    UfastValidatorsService,
    UfastValidatorsRuleService,
    UfastTabsetRouteService,
    IndexpicService,
    UfastUtilService,
    NavigationService,
    LodopPrintService,
    PrintService,
    DictionaryService,
    WorkBoardService,
    UserLeaveService,
    AuthService,
    RoleService,
    GoodsCategoryService,
    GoodsAttributeService,
    GoodsService,
    WarehouseService

  ]
})
export class CoreModule {
}
