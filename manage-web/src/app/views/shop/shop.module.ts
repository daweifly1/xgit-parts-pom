import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {LayoutModule} from "../../layout/layout.module";
import {DirectivesModule} from "../../directives/directives.module";
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {NgZorroAntdModule} from 'ng-zorro-antd';
import {ShopIndexComponent} from "./shop-index/shop-index.component";
import {ListComponent} from "./shop-index/list/list.component";
import {ShopRoutingModule} from "./shop-routing.module";

@NgModule({
  imports: [
    CommonModule,
    ReactiveFormsModule,
    FormsModule,
    NgZorroAntdModule,
    LayoutModule,
    DirectivesModule,
    ShopRoutingModule
  ],
  declarations: [
    ShopIndexComponent,
    ListComponent
  ]
})
export class ShopModule {
}
