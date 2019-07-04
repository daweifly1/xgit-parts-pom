import {AfterViewInit, ChangeDetectorRef, Component, enableProdMode, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {ShowMessageService} from '../../widget/show-message/show-message';
import {MenuService, MenuServiceNs} from '../../core/common-services/menu.service';
import {UserService, UserServiceNs} from '../../core/common-services/user.service';
import {UfastTabsetRouteService} from '../../core/infra/ufast-tabset-route.service';
import {LodopPrintService} from '../../core/infra/lodop-print.service';
import 'rxjs/add/operator/filter';
import {environment} from '../../../environments/environment';

enableProdMode();

@Component({
  selector: 'app-main-layout',
  styleUrls: ['./main-layout.component.scss'],
  templateUrl: './main-layout.component.html'
})
export class MainLayoutComponent implements OnInit, AfterViewInit {


  hideSideMenu: boolean;
  mainMenu: MenuServiceNs.MenuAuthorizedItemModel[];
  sideMenu: MenuServiceNs.MenuAuthorizedItemModel;
  selectedItem: string;
  selectedIndex: number;
  username: string;
  sideLoading: boolean;
  hasLogin: boolean;

  constructor(private messageService: ShowMessageService, private detectorRef: ChangeDetectorRef,
              private menuService: MenuService, public router: Router, public userService: UserService,
              public tabsetService: UfastTabsetRouteService, private lodopService: LodopPrintService
  ) {
    this.sideLoading = false;
    this.selectedIndex = null;
    this.mainMenu = [];
    this.sideMenu = {name: '', url: '', children: [], auths: []};
    this.hideSideMenu = false;
    this.hasLogin = false;
    this.menuService.menuNavChange.subscribe((presentMenu: MenuServiceNs.MenuAuthorizedItemModel[]) => {
      if (presentMenu.length === 0) {
        return;
      }
      this.sideMenu = presentMenu[0];
      // console.log( JSON.stringify(this.sideMenu))
      this.hideSideMenu = false;
      this.selectedItem = this.sideMenu.url;
    });
  }

  public onSelectedIndex(index: number) {
    this.tabsetService.onSelectedIndexChange(index);
  }

  public topNavigate(url: string, checked: boolean) {
    if (checked) {
      return;
    }
    // 一级菜单路由必须是绝对路径
    this.tabsetService.navigateByUrl(url).then(() => {
      this.hideSideMenu = false;
    }, () => {
    });

  }

  public navigateUserInfo() {
    this.router.navigateByUrl('/main/personal/personalInfo');
  }

  public navigatePassword() {
    this.router.navigateByUrl('/main/personal/modifyPwd');
  }

  public logOut() {
    this.userService.logout().subscribe(() => {
      // this.router.navigateByUrl('/login');
      window.location.href = environment.otherData.defaultPath;
    }, (error: any) => {
      console.log(error);
      // this.router.navigateByUrl('/login');
      window.location.href = environment.otherData.defaultPath;
    });
  }

  ngOnInit() {
    this.userService.getLogin().subscribe((resData: UserServiceNs.UfastHttpAnyResModel) => {
      if (resData.status !== 0) {
        //TODO 去登录页面
        // this.messageService.showAlertMessage('', resData.message, 'warning');
        this.router.navigate(['/main/workBoard']);
      } else {
        this.hasLogin = true;
        this.getMenu();
      }
    }, (error: any) => {
      this.messageService.showAlertMessage('', error.message, 'error');
    });
    // this.lodopService.initPrinter().then(() => { }, () => {
    //   console.log('lodop初始化失败');
    // });
  }

  private getMenu() {
    this.messageService.showLoading('');
    this.menuService.getMenuAuthorized()
      .subscribe((menuData: MenuServiceNs.UfastHttpAnyResModel) => {
        this.messageService.closeLoading();
        if (menuData.status !== 0) {
          this.messageService.showAlertMessage(menuData.message, '', 'warning');
          return;
        }
        this.mainMenu = menuData.data;
        this.detectorRef.detectChanges();
      }, (error) => {
        this.messageService.closeLoading();
      });
  }

  public logoClick() {
    this.router.navigate(['/main/workBoard']);
  }

  ngAfterViewInit() {
    if (this.hasLogin) {
      this.getMenu();
    }
  }

}
