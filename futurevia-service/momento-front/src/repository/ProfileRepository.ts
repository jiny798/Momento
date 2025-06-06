import UserProfile from '@/entity/user/UserProfile'
import { instanceToPlain, plainToInstance } from 'class-transformer'
import { singleton } from 'tsyringe'

@singleton()
export default class ProfileRepository {
  public setProfile(profile: UserProfile) {
    const json = instanceToPlain(profile)
    localStorage.setItem('profile', JSON.stringify(json))
  }

  public getProfile(): UserProfile | null {
    const json = localStorage.getItem('profile')
    if (!json) return null

    try {
      const plain = JSON.parse(json)
      return plainToInstance(UserProfile, plain)
    } catch (e) {
      console.error('Failed to parse profile from localStorage:', e)
      return null
    }
  }

  public clear() {
    localStorage.clear()
  }
}
