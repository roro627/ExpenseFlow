import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

type MessageType = 'success' | 'error';
export interface UiMessage {
  type: MessageType;
  text: string;
}

@Injectable({ providedIn: 'root' })
export class NotificationService {
  private messageSubject = new BehaviorSubject<UiMessage | null>(null);
  readonly message$ = this.messageSubject.asObservable();

  success(text: string) {
    this.emit('success', text);
  }

  error(text: string) {
    this.emit('error', text);
  }

  clear() {
    this.messageSubject.next(null);
  }

  private emit(type: MessageType, text: string) {
    this.messageSubject.next({ type, text });
    setTimeout(() => {
      if (this.messageSubject.value?.text === text) {
        this.clear();
      }
    }, 3500);
  }
}
