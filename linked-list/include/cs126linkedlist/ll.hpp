// Copyright (c) 2020 CS126SP20. All rights reserved.

#include "ll.h"

#include <cassert>
#include <utility>
#include <vector>

// TODO(you): Implement these methods.

namespace cs126linkedlist {

template <typename ElementType>
LinkedList<ElementType>::LinkedList() {
  head_ = NULL;
  tail_ = NULL;
  length_ = 0;
}

template <typename ElementType>
LinkedList<ElementType>::LinkedList(const std::vector<ElementType>& values) {
  Node** temp = &head_;
  length_ = values.size();

  for (size_t i = 0; i < values.size(); i++) {
    (*temp) = new Node(values[i]);
    temp = &((*temp)->next);
  }
}

// Copy constructor
template <typename ElementType>
LinkedList<ElementType>::LinkedList(const LinkedList<ElementType>& source) {
  _copy(source);
}

// Move constructor
template <typename ElementType>
LinkedList<ElementType>::LinkedList(LinkedList<ElementType>&& source) noexcept {
  length_ = source.length_;
  source.length_ = 0;
  head_ = source.head_;
  source.head_ = NULL;
  tail_ = source.tail_;
  source.tail_ = NULL;
}

// Destructor
template <typename ElementType>
LinkedList<ElementType>::~LinkedList() {
  _destroy();
}

// Copy assignment operator
template <typename ElementType>
LinkedList<ElementType>& LinkedList<ElementType>::operator=(
    const LinkedList<ElementType>& source) {
  if (this != &source) {
    _destroy();
    _copy(source);
  }
  return *this;
}

// Move assignment operator
template <typename ElementType>
LinkedList<ElementType>& LinkedList<ElementType>::operator=(
    LinkedList<ElementType>&& source) noexcept {
  std::swap(head_, source.head_);
  std::swap(tail_, source.tail_);
  std::swap(length_, source.length_);
  return *this;
}

template <typename ElementType>
void LinkedList<ElementType>::push_front(const ElementType& value) {
  Node * newNode = new Node(value);
  newNode->next = head_;
  newNode->prev = NULL;

  // If list is not empty
  if (head_ != NULL) {
    head_->prev = newNode;
  }
  // If head = tail
  if (tail_ == NULL) {
    tail_ = newNode;
  }

  head_ = newNode;
  length_++;
}

template <typename ElementType>
void LinkedList<ElementType>::push_back(const ElementType& value) {
  Node* newNode = new Node(value);
  newNode->next = NULL;
  newNode->prev = tail_;

  // If list is not empty
  if (tail_ != NULL) {
    tail_->next = newNode;
  }
  // If head = tail
  if (head_ == NULL) {
    head_ = newNode;
  }

  tail_ = newNode;
  length_++;
}

template <typename ElementType>
ElementType LinkedList<ElementType>::front() const {
  if (head_ == NULL) {
    throw std::invalid_argument("List is empty");
  }
  return this->head_->data;
}

template <typename ElementType>
ElementType LinkedList<ElementType>::back() const {
  if (head_ == NULL) {
    throw std::invalid_argument("List is empty");
  }
  return this->tail_->data;
}

template <typename ElementType>
void LinkedList<ElementType>::pop_front() {
  // If empty, do nothing
  if (head_ == NULL) {
    return;
  }
  if (head_) {
    Node *temp = head_;
    head_ = head_->next;
    //temp->prev = NULL;
    delete temp;
    length_--;
  }
}

template <typename ElementType>
void LinkedList<ElementType>::pop_back() {
  // If empty, do nothing
  if (head_ == NULL) {
    return;
  }
  if (tail_) {
    Node *temp = tail_;
    tail_ = tail_->prev;

    if (tail_) {
      tail_->next = NULL;
    }
    else {
      head_ = NULL;
    }

    delete temp;
    length_--;
  }
}

template <typename ElementType>
size_t LinkedList<ElementType>::size() const {
  return length_;
}

template <typename ElementType>
bool LinkedList<ElementType>::empty() const {
  return head_ == NULL && tail_ == NULL && length_ == 0;
}

template <typename ElementType>
void LinkedList<ElementType>::clear() {
  _destroy();
}

template <typename ElementType>
std::ostream& operator<<(std::ostream& os,
                         const LinkedList<ElementType>& list) {
  list.print(os);
  return os;
}

template <typename ElementType>
void LinkedList<ElementType>::RemoveNth(size_t n) {
  if (head_ == NULL || n <= 0 || n > length_) {
    throw std::invalid_argument("Empty list or invalid n");
  }

  Node *temp = head_;

  // Traverse to the nth node
  for (size_t i = 1; temp != NULL && i < n; i++) {
    temp = temp->next;
  }

  if (temp == NULL) {
    return;
  }

  // If the deleted node is the head node
  if (head_ == temp) {
    head_ = temp->next;
  }

  // If deleted node is not head node
  if (temp->next != NULL) {
    temp->prev->next = temp->next;
  }
  free(temp);

  length_--;
}

template <typename ElementType>
void LinkedList<ElementType>::RemoveOdd() {
  if (head_ == NULL) {
    throw std::invalid_argument("List is empty");
  }

  Node *temp = head_;
  Node *temp2 = head_->next;

  // While there is next
  while (temp != NULL && temp2 != NULL)
  {
    // Change next link
    temp->next = temp2->next;

    free(temp2);

    // Go to next node
    temp = temp->next;
    if (temp != NULL) {
      temp2 = temp->next;
    }
    length_--;
  }
}

template <typename ElementType>
bool LinkedList<ElementType>::operator==(
    const LinkedList<ElementType>& rhs) const {
  if (length_ != rhs.length_) {
    return false;
  }
  // If both list are empty
  if (head_ == NULL && rhs.head_ == NULL) {
    return true;
  }
  else {
    Node *temp = head_;
    Node *rhsTemp = rhs.head_;
    // Check every node
    while (temp != NULL) {
      // If not the same
      if (temp->data != rhsTemp->data) {
        return false;
      }
      // Go to next node
      temp = temp->next;
      rhsTemp = rhsTemp->next;
    }
    return true;
  }
}

template <typename ElementType>
bool LinkedList<ElementType>::operator!=(
    const LinkedList<ElementType>& rhs) const {
  return !(*this == rhs);
}

template <typename ElementType>
typename LinkedList<ElementType>::iterator& LinkedList<ElementType>::iterator::
operator++() {
  this->current_ = this->current_->next;
  return *this;
}

template <typename ElementType>
ElementType& LinkedList<ElementType>::iterator::operator*() const {
  return current_->data;
}

template <typename ElementType>
bool LinkedList<ElementType>::iterator::operator!=(
    const LinkedList<ElementType>::iterator& other) const {
  return current_ != other.current_;
}

template <typename ElementType>
typename LinkedList<ElementType>::iterator LinkedList<ElementType>::begin() {
  return iterator(head_);
}

template <typename ElementType>
typename LinkedList<ElementType>::iterator LinkedList<ElementType>::end() {
  return iterator(NULL);
}

template <typename ElementType>
typename LinkedList<ElementType>::const_iterator&
LinkedList<ElementType>::const_iterator::operator++() {
  this->current_ = this->current_->next;
  return *this;
}

template <typename ElementType>
const ElementType& LinkedList<ElementType>::const_iterator::operator*() const {
  return current_->data;
}

template <typename ElementType>
bool LinkedList<ElementType>::const_iterator::operator!=(
    const LinkedList<ElementType>::const_iterator& other) const {
  return current_ != other.current_;
}

template <typename ElementType>
typename LinkedList<ElementType>::const_iterator
LinkedList<ElementType>::begin() const {
  return const_iterator(head_);
}

template <typename ElementType>
typename LinkedList<ElementType>::const_iterator LinkedList<ElementType>::end()
    const {
  return const_iterator(NULL);
}

template <typename ElementType>
void LinkedList<ElementType>::_copy(LinkedList<ElementType> const& source) {
  // set up the default, empty list
  head_ = NULL;
  tail_ = NULL;
  length_ = 0;

  // if we have things to copy
  if (!source.empty()) {
    Node* curr = source.head_;
    Node* prev = NULL;
    // iterate down the parameter list
    while (curr != NULL) {
      Node* node = new Node(curr->data);

      // set the head of the new list
      if (head_ == NULL) {
        head_ = node;
      }

      // correct pointer of the previous node if it exists
      if (prev != NULL) {
        prev->next = node;
      }

      node->prev = prev;

      prev = node;
      curr = curr->next;
    }

    // prev will contain our new tail---set it up accordingly
    tail_ = prev;
    tail_->next = NULL;
    length_ = source.length_;
  }
}

template <typename ElementType>
void LinkedList<ElementType>::_destroy() {
  Node *newNode = head_;
  Node *newNode2;

  if (newNode == NULL) {
    return;
  }
  // Go through every node
  while (newNode != NULL) {
    newNode2 = newNode->next;
    delete newNode;
    newNode = newNode2;
  }

  length_ = 0;
}

template <typename ElementType>
void LinkedList<ElementType>::print(std::ostream& os) const {
  os << "<";
  Node* temp = head_;
  // Go through every node
  while (temp != NULL) {
    os << " " << temp->data;
    temp = temp->next;
  }
  os << " >";
}

template <typename ElementType>
LinkedList<ElementType>::Node::Node() : next(NULL), prev(NULL), data(ElementType()) {}

template <typename ElementType>
LinkedList<ElementType>::Node::Node(const ElementType& newdata) : next(NULL), prev(NULL), data(newdata) {}

}  // namespace cs126linkedlist
