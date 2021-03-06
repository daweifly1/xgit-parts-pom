import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {ActivatedRoute, Router} from '@angular/router';
import {UserServiceNs, UserService} from '../../../core/common-services/user.service';
import {ShowMessageService} from '../../../widget/show-message/show-message';
import {UfastValidatorsService} from '../../../core/infra/validators/validators.service';

@Component({
  selector: 'app-personal-info',
  templateUrl: './personal-info.component.html',
  styleUrls: ['./personal-info.component.scss']
})
export class PersonalInfoComponent implements OnInit {

  userInfo: UserServiceNs.UserInfoModel;
  personalForm: FormGroup;

  constructor(private userService: UserService, private messageService: ShowMessageService, private activeRouter: ActivatedRoute,
              private formBuilder: FormBuilder, private validator: UfastValidatorsService, private router: Router
  ) {
    this.userInfo = {
      locked: null,
      username: '',
      name: '',
      roleIds: null,
      sex: null,
      deptId: '',
    };
  }

  public updateInfo() {
    for (let key in this.personalForm.controls) {
      this.personalForm.controls[key].markAsDirty();
      this.personalForm.controls[key].updateValueAndValidity();
    }
    if (this.personalForm.invalid) {
      return;
    }
    let reqData: any = {};
    for (let item in this.personalForm.value) {
      reqData[item] = this.personalForm.value[item];
    }
    this.userService.updatePersonInfo(reqData).subscribe((resData: UserServiceNs.UfastHttpAnyResModel) => {
      if (resData.status === 0) {
        this.messageService.showToastMessage('操作成功', 'success');
        this.personalForm.reset();
        this.getPersonalInfo();
      } else {
        this.messageService.showAlertMessage('', resData.message, 'warning');
      }
    }, (error: any) => {
      this.messageService.showAlertMessage('', error.message, 'error');
    });
  }

  public getPersonalInfo() {
    this.userService.getLogin().subscribe((resData: UserServiceNs.UfastHttpAnyResModel) => {
      if (resData.status === 0) {
        this.userInfo = {
          locked: resData.data.locked,
          username: resData.data.username,
          name: resData.data.name,
          roleIds: resData.data.roleIds,
          sex: resData.data.sex,
          deptId: resData.data.deptId,
          nickname: resData.data.nickname,
          deptName: resData.data.deptName,
          email: resData.data.email,
          mobile: resData.data.mobile,
          telephone: resData.data.telephone,
          roleNames: resData.data.roleNames
        };
        this.personalForm.setValue({
          name: this.userInfo.name,
          email: this.userInfo.email,
          mobile: this.userInfo.mobile
        });
      } else {
        this.messageService.showAlertMessage('', resData.message, 'warning');
      }
    });
  }

  public navigatePd() {
    this.router.navigate(['../modifyPwd'], {relativeTo: this.activeRouter});
  }

  ngOnInit() {

    this.personalForm = this.formBuilder.group({
      name: [null, [Validators.required, Validators.maxLength(20)]],
      email: [null, [this.validator.emailValidator()]],
      mobile: [null, [this.validator.mobileValidator()]]
    });

    this.getPersonalInfo();
  }

}
