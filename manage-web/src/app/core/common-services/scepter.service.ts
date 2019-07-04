import {Injectable, Injector} from '@angular/core';
import {HttpUtilNs, HttpUtilService} from '../infra/http/http-util.service';
import {MenuServiceNs} from './menu.service';
import {Observable} from 'rxjs/Observable';
import {map, shareReplay} from 'rxjs/operators';

export namespace ScepterServiceNs {
  export interface ScepterResModelT<T> extends HttpUtilNs.UfastHttpRes {
    value: T;
    data?: T;
  }

  export interface AuthCodeObj {
    [index: string]: boolean;
  }

  export interface RoleModel {
    deptId?: string;
    deptName?: string;
    id?: string;
    platformId?: number;
    name: string;
    remark?: string;
    spaceId?: string;
    type?: number;
    checked?: boolean;
  }

  export interface GetRoleResModel extends HttpUtilNs.UfastHttpResT<RoleModel[]> {
  }

  export interface EditRoleModel {
    deptId?: string;
    deptName?: string;
    id: string;
    name?: string;
    remark?: string;
    spaceId?: string;
    type: number;
  }

  export interface GetMenusAuthsResModel extends HttpUtilNs.UfastHttpRes {
    auths: number[];
    menus: number[];
  }

  export interface AddMenusAuthsModel {
    authIds: number[];
    menuIds: number[];
    channel?: number[];
    roleId: string;
  }

  export interface MenuShownItemModel extends MenuServiceNs.MenuAuthorizedItemModel {
  }


  export class ScepterServiceClass {
    private http: HttpUtilService;
    private cacheAuthCode: Observable<ScepterServiceNs.ScepterResModelT<ScepterServiceNs.AuthCodeObj>>;

    constructor(injector: Injector) {
      this.http = injector.get(HttpUtilService);
      this.cacheAuthCode = null;
    }

    public getRoles(): Observable<GetRoleResModel> {
      const config: HttpUtilNs.UfastHttpConfig = {};
      config.gateway = HttpUtilNs.GatewayKey.Ius;
      return this.http.Get<GetRoleResModel>('/sysRole/list', null, config);
    }

    public addRole(role: RoleModel) {
      const config: HttpUtilNs.UfastHttpConfig = {};
      config.gateway = HttpUtilNs.GatewayKey.Ius;
      return this.http.Post<ScepterResModelT<any>>('/sysRole/save', role, config);
    }

    public deleteRoles(roleIds: string[]) {
      const config: HttpUtilNs.UfastHttpConfig = {};
      config.gateway = HttpUtilNs.GatewayKey.Ius;
      return this.http.Post<ScepterResModelT<any>>('/sysRole/delete', roleIds, config);
    }

    public editRoles(roleInfo: EditRoleModel) {
      const config: HttpUtilNs.UfastHttpConfig = {};
      config.gateway = HttpUtilNs.GatewayKey.Ius;
      return this.http.Post<ScepterResModelT<any>>('/scepter/editRole', roleInfo, config);
    }

    public getMenusAuths(roleId: string) {
      const config: HttpUtilNs.UfastHttpConfig = {};
      config.gateway = HttpUtilNs.GatewayKey.Ius;
      return this.http.Get<GetMenusAuthsResModel>('/scepter/getMenusAuths', {roleId: roleId}, config);
    }

    public getMenuShown() {
      const config: HttpUtilNs.UfastHttpConfig = {};
      config.gateway = HttpUtilNs.GatewayKey.Ius;
      return this.http.Get<ScepterResModelT<MenuShownItemModel[]>>('/menu/shown', null, config);
    }

    public addMenusAuths(auths: AddMenusAuthsModel) {
      const config: HttpUtilNs.UfastHttpConfig = {};
      config.gateway = HttpUtilNs.GatewayKey.Ius;
      return this.http.Post<ScepterResModelT<any>>('/scepter/addMenusAuths', auths, config);
    }

    public getAuthCodes() {
      const config: HttpUtilNs.UfastHttpConfig = {
        gateway: HttpUtilNs.GatewayKey.Ius
      };
      if (!this.cacheAuthCode) {
        this.cacheAuthCode = this.http.Post('/menu/getAuthIds', {platformId: 1}, config)
          .pipe(map((resData: any) => {
            if (resData.status === 0 && resData.data instanceof Array) {
              const temp = {};
              resData.data.forEach((item) => {
                temp[item] = true;
              });
              resData.data = temp;
            } else {
              resData.data = {};
            }
            return resData;
          }), shareReplay(1));
      }

      return this.cacheAuthCode;
    }

    // public getRoles() {
    //   return this.http.get<GetRoleResModel>('ius', '/scepter/roles');
    // }
    //
    // public addRole(role: RoleModel) {
    //   return this.http.post<ScepterResModelT<any>>('ius', '/scepter/role', role);
    // }
    //
    // public deleteRoles(roleIds: string[]) {
    //   return this.http.post<ScepterResModelT<any>>('ius', '/scepter/deleteRoles', roleIds);
    // }
    //
    // public editRoles(roleInfo: EditRoleModel) {
    //   return this.http.post<ScepterResModelT<any>>('ius', '/scepter/editRole', roleInfo);
    // }
    //
    // public getMenusAuths(roleId: string) {
    //   return this.http.get<GetMenusAuthsResModel>('ius', '/scepter/getMenusAuths', {roleId: roleId});
    // }
    //
    // public getMenuShown() {
    //   return this.http.get<ScepterResModelT<MenuShownItemModel[]>>('ius', '/menu/shown');
    // }
    //
    // public addMenusAuths(auths: AddMenusAuthsModel) {
    //   return this.http.post<ScepterResModelT<any>>('ius', '/scepter/addMenusAuths', auths);
    // }
  }


}

@Injectable()
export class ScepterService extends ScepterServiceNs.ScepterServiceClass {
  constructor(injector: Injector) {
    super(injector);
  }
}

