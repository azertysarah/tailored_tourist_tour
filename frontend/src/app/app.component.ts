import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'frontend';

  monument = "";

  researchForm = new FormGroup({
    location: new FormControl('', Validators.required),
    period: new FormControl('', Validators.required),
    time: new FormControl(null, Validators.required),
    budget: new FormControl(null)
  })

  constructor(
    private http: HttpClient
  ) {}

  onSubmit() {
    return this.http.post('http://localhost:8080/tours', this.researchForm.value).subscribe({
      next: (reponses) => {
        const monuments = reponses as string[];
        const time = this.researchForm.get('time')?.value
        console.log(reponses);
        if (monuments && monuments.length > 0  && time) {
          this.monument = "Vous pouvez visiter pendant " + time + " de jour(s) : ";
          this.monument += monuments.reduce((prev, next) => prev + ', ' + next) + '.';
        }
      },
      error: (err: any) => this.monument = ""
    });
  }
}
