import { Component, OnInit } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { AuthService } from 'src/app/services/auth.service';
import { UserService } from 'src/app/services/user.service';
import { User } from 'src/app/site/user';
import { switchMap } from 'rxjs/operators';

@Component({
  selector: 'app-change-password',
  templateUrl: './change-password.component.html',
  styleUrls: ['./change-password.component.css']
})
export class ChangePasswordComponent implements OnInit {
  newUser: User;
  success: boolean = false;
  failure: boolean = false;
  error: boolean = false;

  editForm: FormGroup;
  constructor(private authService: AuthService, private userService: UserService) { }

  ngOnInit() {
    this.editForm = new FormGroup({
      'username': new FormControl('', [
        Validators.required
      ]),
      'password': new FormControl('', [
        Validators.required
      ]),
      'newPassword': new FormControl('', [
        Validators.required
      ])
    });

    this.authService.loggedInUser.subscribe((user) => {
      this.username.setValue(user.userId);
    })
  }

  editPassword() {
    console.log(this.editForm.value);
    this.newUser = this.authService.loggedInUser.value;
    console.log(this.newUser);
    this.userService.changePassword(this.username.value, this.password.value, this.newPassword.value).subscribe((user) => {
      if (user) {
        this.success = true;
        this.failure = false;
        this.error = false;
        this.newUser = user;
      }
      else {
        this.failure = true;
        this.success = false;
        this.error = false;
      }
    }, () => {
      this.error = true;
      this.success = false;
      this.failure = false;
      console.log("Error checking password");
    }, () => {
      console.log("Finally finish");
      this.authService.loggedInUser.next(this.newUser);
    })
    /* this.userService.checkPassword(this.username.value, this.password.value).pipe(
      switchMap((user) => {
        console.log(user);
        if (user) {
          console.log("Password is correct");
          this.newUser.password = this.newPassword.value;
          //console.log(this.newUser.password);
          this.success = true;
          this.failure = false;
          this.error = false;

          return this.userService.updateUser(this.newUser);
        }
        else {
          console.log("Password is incorrect !!");
          this.failure = true;
          this.success = false;
          this.error = false;

          return this.userService.getUser(this.newUser.userId);
        }
      })
    ).subscribe((response) => {
      console.log("Success updating password");
    }, () => {
      this.error = true;
      this.success = false;
      this.failure = false;
      console.log("Error checking password");
    }, () => {
      console.log("Finally finish");
      this.authService.loggedInUser.next(this.newUser);
    }) */
  }

  get username() {
    return this.editForm.get('username');
  }

  get password() {
    return this.editForm.get('password');
  }

  get newPassword() {
    return this.editForm.get('newPassword');
  }

}
