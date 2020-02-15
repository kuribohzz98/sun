import { Component, Input } from '@angular/core';

@Component({
  selector: 'jhi-login-register',
  templateUrl: './login-register.component.html'
})
export class LoginRegisterComponent {
  @Input() idModel: string = '';
  private isLogin: boolean = true;

  constructor() {}

  changTabSet() {
    this.isLogin = !this.isLogin;
  }
}
