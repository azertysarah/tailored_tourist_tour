import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { T3Service } from './t3.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  title = 'frontend';

  // periods: any;
  // communes: any;
  result: any;

  researchForm = new FormGroup({
    monument: new FormControl(''),
    commune: new FormControl(''),
    period: new FormControl(''),
    time: new FormControl(null)
  })

  constructor(
    private t3service: T3Service
  ) {}

  ngOnInit(): void {
    // this.getAllPeriods();
    // this.getAllCommunes();
  }

  // getAllPeriods() {
  //   this.t3service.getAllPeriods().subscribe({
  //     next: (res: any) => this.periods = res,
  //     error: () => console.warn('An error occured')
  //   });
  // }

  // getAllCommunes(){
  //   this.t3service.getAllCommunes().subscribe({
  //     next: (res: any) => this.communes = res,
  //     error: () => console.warn('An error occured')
  //   });
  // }

  onSubmit() {
    this.t3service.postSearch(this.researchForm.value).subscribe({
      next: (res: any) => {
        console.log(res);
        this.result = res;
        // const time = this.researchForm.get('time')?.value
        // if (monuments && monuments.length > 0  && time) {
        //   this.result = "Vous pouvez visiter pendant " + time + " de jour(s) : ";
        //   this.result += monuments.reduce((prev, next) => prev + ', ' + next) + '.';
        // }
      },
      error: () => this.result = "An error occured ..."
    });
  }
}
