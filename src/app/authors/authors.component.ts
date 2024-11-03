import { Component } from "@angular/core";
import { Router, RouterOutlet } from "@angular/router";
import { ActivatedRoute } from "@angular/router";
import { FormsModule } from "@angular/forms";
import { NgIf } from "@angular/common";

@Component({
  selector: "app-authors",
  templateUrl: "./authors.component.html",
  styleUrls: ["./authors.component.css"],
  standalone: true,
  imports: [RouterOutlet, FormsModule, NgIf],
})
export class AuthorsComponent {
  authorId: string = "";
  message: string = "";
  showMessage = false;

  constructor(
    private router: Router,
    private route: ActivatedRoute,
  ) {}

  submit(): void {
    if (!this.authorId.trim()) {
      this.message = "Please enter an author ID";
      this.showMessage = true;
      setTimeout(() => (this.showMessage = false), 3000);
      return;
    }
    this.router
      .navigate(["./", this.authorId], { relativeTo: this.route })
      .then((r) => {});
  }
}
