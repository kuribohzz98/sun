import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { IUserDeliveryInfo, UserDeliveryInfo } from 'app/shared/model/user-delivery-info.model';
import { UserDeliveryInfoService } from './user-delivery-info.service';
import { IUser } from 'app/core/user/user.model';
import { UserService } from 'app/core/user/user.service';

@Component({
  selector: 'jhi-user-delivery-info-update',
  templateUrl: './user-delivery-info-update.component.html'
})
export class UserDeliveryInfoUpdateComponent implements OnInit {
  isSaving = false;

  users: IUser[] = [];

  editForm = this.fb.group({
    id: [],
    userId: [null, [Validators.required]],
    phone: [null, [Validators.required]],
    city: [null, [Validators.required]],
    district: [null, [Validators.required]],
    ward: [null, [Validators.required]],
    address: []
  });

  constructor(
    protected userDeliveryInfoService: UserDeliveryInfoService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ userDeliveryInfo }) => {
      this.updateForm(userDeliveryInfo);

      this.userService
        .query()
        .pipe(
          map((res: HttpResponse<IUser[]>) => {
            return res.body ? res.body : [];
          })
        )
        .subscribe((resBody: IUser[]) => (this.users = resBody));
    });
  }

  updateForm(userDeliveryInfo: IUserDeliveryInfo): void {
    this.editForm.patchValue({
      id: userDeliveryInfo.id,
      userId: userDeliveryInfo.userId,
      phone: userDeliveryInfo.phone,
      city: userDeliveryInfo.city,
      district: userDeliveryInfo.district,
      ward: userDeliveryInfo.ward,
      address: userDeliveryInfo.address
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const userDeliveryInfo = this.createFromForm();
    if (userDeliveryInfo.id !== undefined) {
      this.subscribeToSaveResponse(this.userDeliveryInfoService.update(userDeliveryInfo));
    } else {
      this.subscribeToSaveResponse(this.userDeliveryInfoService.create(userDeliveryInfo));
    }
  }

  private createFromForm(): IUserDeliveryInfo {
    return {
      ...new UserDeliveryInfo(),
      id: this.editForm.get(['id'])!.value,
      userId: this.editForm.get(['userId'])!.value,
      phone: this.editForm.get(['phone'])!.value,
      city: this.editForm.get(['city'])!.value,
      district: this.editForm.get(['district'])!.value,
      ward: this.editForm.get(['ward'])!.value,
      address: this.editForm.get(['address'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IUserDeliveryInfo>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }

  trackById(index: number, item: IUser): any {
    return item.id;
  }
}
