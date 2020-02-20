import { NotifierService } from 'angular-notifier';
import { Component, AfterViewInit, Renderer, ElementRef, ViewChild, Input } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { Router } from '@angular/router';
import { LoginService } from 'app/core/login/login.service';
import { NgbActiveModal, NgbModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'jhi-login-modal',
  templateUrl: './login.component.html'
})
export class LoginModalComponent implements AfterViewInit {
  @ViewChild('username', { static: false })
  username?: ElementRef;

  authenticationError = false;

  loginForm = this.fb.group({
    username: [''],
    password: [''],
    rememberMe: [false]
  });

  constructor(
    private loginService: LoginService,
    private renderer: Renderer,
    private router: Router,
    private fb: FormBuilder,
    private notifierService: NotifierService,
    private modalService: NgbModal
  ) {}

  ngAfterViewInit(): void {
    if (this.username) {
      this.renderer.invokeElementMethod(this.username.nativeElement, 'focus', []);
    }
  }

  cancel(): void {
    this.authenticationError = false;
    this.loginForm.patchValue({
      username: '',
      password: ''
    });
  }

  login(): void {
    console.log(this.loginForm);
    this.loginService
      .login({
        username: this.loginForm.get('username')!.value,
        password: this.loginForm.get('password')!.value,
        rememberMe: this.loginForm.get('rememberMe')!.value
      })
      .subscribe(
        () => {
          this.authenticationError = false;
          // if (
          //   this.router.url === '/account/register' ||
          //   this.router.url.startsWith('/account/activate') ||
          //   this.router.url.startsWith('/account/reset/')
          // ) {
          //   this.router.navigate(['']);
          // }
          this.notifierService.show({
            type: 'success',
            message: 'Đăng nhập thành công',
            id: 'login_success'
          });
          this.router.navigate(['']);
          this.modalService.dismissAll();
        },
        () => {
          this.notifierService.show({
            type: 'error',
            message: 'Tên đăng nhập hoặc mật khẩu không chính xác',
            id: 'login_faild'
          });
          return (this.authenticationError = true);
        }
      );
  }

  // register(): void {
  //   this.router.navigate(['/account/register']);
  // }

  requestResetPassword(): void {
    this.router.navigate(['/account/reset', 'request']);
  }
}
