import {Component, Input, OnInit,} from '@angular/core';

@Component({
  selector: 'app-list',
  templateUrl: './list.component.html',
  styleUrls: ['./list.component.scss']
})
export class ListComponent implements OnInit {
  @Input() username: any;
  shops: any;

  constructor() {
    this.shops = [
      {
        id: 1,
        title: '徐工信息-总店',
        status: 100,
        statusText: '已认证'
      },
      {
        id: 2,
        title: '汉旗-分店',
        status: 200,
        statusText: '去认证'
      },
      {
        id: 3,
        title: '大禹-分店',
        status: 300,
        statusText: '审核中'
      },
      {
        id: 4,
        title: '雾里-分店',
        status: 400,
        statusText: '未通过',
        message: '拍照不清晰。'
      },
      {
        id: 5,
        title: '飞雪-分店',
        status: 300,
        statusText: '审核中'
      }
    ];
  }

  ngOnInit() {
  }

}
