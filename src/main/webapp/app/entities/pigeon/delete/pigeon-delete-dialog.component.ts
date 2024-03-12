import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IPigeon } from '../pigeon.model';
import { PigeonService } from '../service/pigeon.service';

@Component({
  standalone: true,
  templateUrl: './pigeon-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class PigeonDeleteDialogComponent {
  pigeon?: IPigeon;

  constructor(
    protected pigeonService: PigeonService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.pigeonService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
