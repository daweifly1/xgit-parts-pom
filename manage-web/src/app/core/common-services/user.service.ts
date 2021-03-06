import {Injectable, Injector} from '@angular/core';
import {HttpUtilNs, HttpUtilService} from '../infra/http/http-util.service';
import {Observable} from 'rxjs/Observable';
import {map, retry} from 'rxjs/operators';


export namespace UserServiceNs {
  import UfastHttpRes = HttpUtilNs.UfastHttpRes;

  export interface HttpError extends HttpUtilNs.UfastHttpRes {
  }

  export interface UfastHttpResT<T> extends HttpUtilNs.UfastHttpResT<T> {
  }

  export interface AuthAnyResModel extends HttpUtilNs.UfastHttpRes {
    value: any;
  }

  export interface UfastHttpAnyResModel extends HttpUtilNs.UfastHttpRes {
    data?: any;
  }

  export interface AuthInfoResModel extends HttpUtilNs.UfastHttpRes {
    data: {
      authId: string;
      verifyCode?: string;
      verifyImgUrl: string;
    };
  }

  export interface AuthLoginReqModel {
    authId: string;
    code: string;
    username: string;
    password: string;
  }

  export interface AuthUpdateInfoReqModel {
    email?: string;
    mobile?: string;
    areaCode: string;
    username: string;
    name: string;
    password: string;
    telephone: string;
  }

  export interface AuthLoginInfoValueModel {
    username: string;
    password: string;
    deptName: string;      // 所属部门
    email: string;
    name: string;
    roleNames: string;     // 角色
    mobile: string;        // 手机号
    telephone: string;     // 电话
    userId: string;
    rolesVOs: any[];
    status: number;
    type: number;

    [otherKey: string]: any;
  }

  export interface AuthLoginInfoResModel extends UfastHttpRes {
    data: AuthLoginInfoValueModel;
  }

  export interface UserInfoModel {
    locked: number;      // 0：启用，1：锁定
    username: string;
    name: string;
    code?: string;
    erpCode?: string;
    roleIds: string[];
    email?: string;
    mobile?: string;
    telephone?: string;
    nickname?: string;   // 昵称
    sex: number;         // 0:女，1：男
    deptId: string;
    areaCode?: string;
    spaceId?: string;
    deptName?: string;
    lastLoginTime?: number;
    roleNames?: string;
    roleVOs?: any[];
    type?: number;
    status?: number;
    userId?: number;
    id?: number;
    superiorId?: string;
    superiorName?: string;
  }

  export interface DepartmentModel {
    code: string;
    id: string;
    leaf: number;
    name: string;
    parentId: string;
    seq: number;
    spaceId: string;
  }

  export class UserServiceClass {
    private http: HttpUtilService;
    public userInfo: any;

    constructor(private injector: Injector) {
      this.http = injector.get(HttpUtilService);
      this.userInfo = {
        username: null
      };
    }

    public getAuthInfo(): Observable<AuthInfoResModel> {
      const config: HttpUtilNs.UfastHttpConfig = {};
      config.gateway = HttpUtilNs.GatewayKey.Ius;
      return this.http.Get<AuthInfoResModel>('/auth/authInfo', null, config)
        .pipe(retry(2), map((data: AuthInfoResModel) => {
          data.data.verifyImgUrl = this.http.getFullUrl('ius', '/auth/kaptcha') + `?authid=${data.data.authId}`;
          return data;
        }));
    }

    public postLogin(loginData: AuthLoginReqModel): Observable<AuthAnyResModel> {
      const config: HttpUtilNs.UfastHttpConfig = {};
      config.gateway = HttpUtilNs.GatewayKey.Ius;
      return this.http.Post<AuthAnyResModel>('/auth/login', loginData, config)
        .pipe(map((resData: AuthAnyResModel) => {
          if (resData.status === 0) {
            this.userInfo.username = loginData.username;
          }
          return resData;
        }));
    }

    public logout(): Observable<AuthAnyResModel> {
      const config: HttpUtilNs.UfastHttpConfig = {};
      config.gateway = HttpUtilNs.GatewayKey.Ius;
      return this.http.Get('/auth/logout', null, config)
        .pipe(map((resData: AuthAnyResModel) => {
          if (resData.status === 0) {
            this.userInfo.username = '';
          }
          return resData;
        }));
    }

    public modifyPassword(oldPassword: string, newPassword: string): Observable<UfastHttpAnyResModel> {
      const config: HttpUtilNs.UfastHttpConfig = {};
      config.gateway = HttpUtilNs.GatewayKey.Ius;
      return this.http.Post<AuthAnyResModel>('/auth/password', {
        newPassword: newPassword,
        oldPassword: oldPassword
      }, config);
    }

    public getLogin(): Observable<UfastHttpAnyResModel> {
      const config: HttpUtilNs.UfastHttpConfig = {};
      config.gateway = HttpUtilNs.GatewayKey.Ius;
      return this.http.Get<AuthLoginInfoResModel>('/auth/userInfo', null, config).pipe(
        map((data: any) => {
            if (data && data.data) {
              if (null != data.data.username) {
                this.userInfo.username = data.data.username;
              }
            }
            return data;
          }
        )
      );
    }

    public updatePersonInfo(data: AuthUpdateInfoReqModel): Observable<UfastHttpAnyResModel> {
      const config: HttpUtilNs.UfastHttpConfig = {};
      config.gateway = HttpUtilNs.GatewayKey.Ius;
      return this.http.Post<AuthAnyResModel>('/profile/update', data, config);
    }

    public addUser(userInfo: UserInfoModel): Observable<UfastHttpAnyResModel> {
      const config: HttpUtilNs.UfastHttpConfig = {};
      config.gateway = HttpUtilNs.GatewayKey.Ius;
      return this.http.Post('/profile', userInfo, config);
    }

    public updateUserInfo(userInfo: UserInfoModel): Observable<UfastHttpAnyResModel> {
      const config: HttpUtilNs.UfastHttpConfig = {};
      config.gateway = HttpUtilNs.GatewayKey.Ius;
      return this.http.Post('/sysAccount/update', userInfo, config);
    }

    public resetPassword(userIdList: string[]): Observable<UfastHttpAnyResModel> {
      const config: HttpUtilNs.UfastHttpConfig = {};
      config.gateway = HttpUtilNs.GatewayKey.Ius;
      return this.http.Post('/account/resetPassword', userIdList, config);
    }

    public removeUsers(userIdList: string[]): Observable<UfastHttpAnyResModel> {
      const config: HttpUtilNs.UfastHttpConfig = {};
      config.gateway = HttpUtilNs.GatewayKey.Ius;
      return this.http.Post('/profile/remove', userIdList, config);
    }

    public getUserDetail(userId: number): Observable<UfastHttpResT<UserInfoModel>> {
      const config: HttpUtilNs.UfastHttpConfig = {};
      config.gateway = HttpUtilNs.GatewayKey.Ius;
      return this.http.Get('/sysAccount/queryAccountById', {userId: userId}, config);
    }

    public getUserList(filter: { filters: any, current: number, pageSize: number }): Observable<UfastHttpAnyResModel> {
      const config: HttpUtilNs.UfastHttpConfig = {};
      config.gateway = HttpUtilNs.GatewayKey.Ius;
      return this.http.Get('/sysAccount/list', filter, config);
    }

    public lockUsers(lock: number, userIds: string[]): Observable<UfastHttpAnyResModel> {
      const config: HttpUtilNs.UfastHttpConfig = {};
      config.gateway = HttpUtilNs.GatewayKey.Ius;
      const body = {
        lock: lock,
        userIds: userIds
      };
      return this.http.Post('/profile/updateLock', body, config);
    }

    public getDepartment(id: string): Observable<HttpUtilNs.UfastHttpResT<DepartmentModel[]>> {
      const config: HttpUtilNs.UfastHttpConfig = {};
      config.gateway = HttpUtilNs.GatewayKey.Ius;
      return this.http.Get('/department/list', {id: id}, config);
    }


    public register(registerInfo: any): Observable<any> {
      const config: HttpUtilNs.UfastHttpConfig = {};
      config.gateway = HttpUtilNs.GatewayKey.Ius;
      return this.http.Get('/auth/regist', registerInfo, config);
    }

    public getCurrentUsername(): void {
      if (!this.userInfo.username) {
        this.getLogin().subscribe((data: UfastHttpAnyResModel) => {
          return data.data.username;
        }, (error: UserServiceNs.HttpError) => {
          return null;
        });
      }
    }
  }
}

@Injectable()
export class UserService extends UserServiceNs.UserServiceClass {
  constructor(injector: Injector) {
    super(injector);
  }
}
