import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {ShopIndexComponent} from "./shop-index/shop-index.component";


const routes: Routes = [
  {path: '', redirectTo: 'list', pathMatch: 'full'},
  {path: 'list', component: ShopIndexComponent}
];

@NgModule({
  imports: [
    RouterModule.forChild(routes)
  ],
  exports: [RouterModule]
})
export class ShopRoutingModule {
}
