import {Component, OnInit} from '@angular/core';
import {UserService} from "../../../core/common-services/user.service";
import {Router} from '@angular/router';

@Component({
  selector: 'app-shop-index',
  templateUrl: './shop-index.component.html',
  styleUrls: ['./shop-index.component.scss']
})
export class ShopIndexComponent implements OnInit {
  dd: any;

  constructor(public router: Router, public userService: UserService) {
    this.dd = '江苏省徐州市鼓楼区科技路6号';
    this.userService.getCurrentUsername();
  }

  ngOnInit() {

  }


}
